package app.discovery.ui;

import app.discovery.domain.User;
import app.discovery.service.FeedService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/** The social experience feed — "what people did last weekend", visual and alive. */
@Route(value = "feed", layout = MainLayout.class)
@PageTitle("Experience feed")
@AnonymousAllowed
public class FeedView extends VerticalLayout {
  private final FeedService feed;
  private final SessionUtil session;
  private final VerticalLayout posts = new VerticalLayout();

  public FeedView(FeedService feed, SessionUtil session) {
    this.feed = feed;
    this.session = session;
    setPadding(true);

    H3 title = new H3("Weekend stories");
    Paragraph sub = new Paragraph("Real photos and reviews from people who went, made, tasted, planted.");
    sub.getStyle().set("color", "var(--lumo-secondary-text-color)");

    User me = session.currentUser();
    VerticalLayout composer = new VerticalLayout();
    composer.setVisible(me != null);
    composer.getStyle().set("background", "var(--lumo-contrast-5pct)")
        .set("border-radius", "16px").set("padding", "16px");
    TextArea text = new TextArea("Share your experience");
    text.setWidthFull();
    text.setPlaceholder("I went to the pasta workshop and...");
    TextField photos = new TextField("Photo URLs (comma separated)");
    photos.setWidthFull();
    Button share = new Button("Share", e -> {
      try {
        List<String> urls = new ArrayList<>();
        for (String u : photos.getValue().split(",")) if (!u.isBlank()) urls.add(u.trim());
        feed.publish(me.getId(), null, text.getValue(), urls);
        text.clear();
        photos.clear();
        load();
        Notification.show("Shared!", 3000, Notification.Position.MIDDLE);
      } catch (Exception ex) {
        Notification.show(ex.getMessage(), 4000, Notification.Position.MIDDLE);
      }
    });
    composer.add(new H4("How was your weekend?"), text, photos, share);

    add(title, sub, composer, posts);
    load();
  }

  private void load() {
    posts.removeAll();
    User me = session.currentUser();
    var page = feed.feed(PageRequest.of(0, 20));
    if (page.isEmpty()) {
      posts.add(new Paragraph("No stories yet — be the first to share a weekend!"));
      return;
    }
    page.forEach(p -> {
      Div card = new Div();
      card.getStyle().set("border-radius", "16px").set("overflow", "hidden")
          .set("background", "var(--lumo-base-color)")
          .set("box-shadow", "var(--lumo-box-shadow-s)").set("max-width", "560px");
      Div head = new Div();
      head.getStyle().set("padding", "12px 16px 0");
      H4 author = new H4(p.getAuthor().getDisplayName());
      author.getStyle().set("margin", "0");
      head.add(author);
      if (p.getWorkshop() != null) {
        Span ws = new Span("at " + p.getWorkshop().getTitle());
        ws.getStyle().set("color", "var(--lumo-primary-text-color)").set("font-size", "0.85em");
        head.add(ws);
      }
      card.add(head);
      p.getPhotos().forEach(ph -> {
        Image img = new Image(ph.getUrl(), "experience photo");
        img.setWidthFull();
        img.getStyle().set("object-fit", "cover").set("max-height", "380px");
        card.add(img);
      });
      Div body = new Div();
      body.getStyle().set("padding", "8px 16px 12px");
      Paragraph text = new Paragraph(p.getText());
      HorizontalLayout actions = new HorizontalLayout();
      Button like = new Button("♥ " + p.getLikeCount(), e -> {
        if (me == null) {
          Notification.show("Log in to like posts");
          return;
        }
        feed.toggleLike(me.getId(), p.getId());
        load();
      });
      like.getStyle().set("border-radius", "999px");
      actions.add(like);
      body.add(text, actions);
      card.add(body);
      posts.add(card);
    });
  }
}
