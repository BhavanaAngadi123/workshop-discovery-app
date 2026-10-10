package app.discovery.ui;

import app.discovery.domain.User;
import app.discovery.domain.WorkshopCategory;
import app.discovery.poster.PosterRenderer;
import app.discovery.service.ListingStudioService;
import app.discovery.service.ListingStudioService.ListingDraft;
import app.discovery.service.WorkshopService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/** Host Studio: AI Listing Studio headline + manual listing path + my workshops. */
@Route(value = "host", layout = MainLayout.class)
@PageTitle("Host Studio")
@RolesAllowed({"HOST", "ADMIN"})
public class HostDashboardView extends VerticalLayout {
  private final ListingStudioService studio;
  private final PosterRenderer posters;
  private final WorkshopService workshops;
  private final SessionUtil session;

  public HostDashboardView(ListingStudioService studio, PosterRenderer posters,
                           WorkshopService workshops, SessionUtil session) {
    this.studio = studio;
    this.posters = posters;
    this.workshops = workshops;
    this.session = session;
    setPadding(true);

    add(new H2("Host Studio"));
    add(aiStudioSection());
    add(new Hr());
    add(new H3("My workshops"));
    refreshMine(new VerticalLayout());
  }

  private VerticalLayout aiStudioSection() {
    VerticalLayout root = new VerticalLayout();
    root.getStyle().set("background", "var(--lumo-contrast-5pct)")
        .set("border-radius", "16px").set("padding", "20px");
    root.add(new H3("✨ AI Listing Studio"),
        new Paragraph("Describe your workshop in plain words — the AI writes your listing copy, "
            + "designs your publicity poster, and sets up booking. Or skip it and post manually below."));

    TextArea idea = new TextArea("Your idea, in your words");
    idea.setWidthFull();
    idea.setPlaceholder("10 pasta-making slots this weekend at my restaurant, $45 per person, "
        + "includes all ingredients and dinner, 2 hours, beginners welcome...");
    HorizontalLayout row1 = new HorizontalLayout();
    ComboBox<WorkshopCategory> category = new ComboBox<>("Category");
    category.setItems(WorkshopCategory.values());
    TextField city = new TextField("City");
    TextField location = new TextField("Venue / location");
    row1.add(category, city, location);
    row1.setWrap(true);

    HorizontalLayout row2 = new HorizontalLayout();
    DateTimePicker when = new DateTimePicker("First session starts");
    when.setValue(LocalDateTime.now().plusDays(2));
    IntegerField durationMin = new IntegerField("Duration (minutes)");
    durationMin.setValue(120);
    IntegerField slots = new IntegerField("Slots");
    slots.setValue(10);
    NumberField fee = new NumberField("Fee per person ($)");
    fee.setValue(45.0);
    TextArea includes = new TextArea("What's included (comma separated)");
    includes.setPlaceholder("all ingredients, dinner, apron to take home");
    row2.add(when, durationMin, slots, fee);
    row2.setWrap(true);

    Select<String> templatePick = new Select<>();
    List<String> tplIds = new ArrayList<>();
    tplIds.add("(let AI choose)");
    posters.templates().forEach(t -> tplIds.add(t.id() + " — " + t.name()));
    templatePick.setItems(tplIds);
    templatePick.setValue("(let AI choose)");
    templatePick.setLabel("Poster style");

    Div preview = new Div();
    preview.getStyle().set("display", "flex").set("gap", "24px").set("flex-wrap", "wrap");

    Button generate = new Button("Generate my listing ✨", e -> {
      try {
        String tpl = templatePick.getValue().startsWith("(") ? null
            : templatePick.getValue().split(" — ")[0];
        var req = new ListingStudioService.DraftRequest(
            idea.getValue(), category.getValue() == null ? null : category.getValue().name(),
            null, slots.getValue(),
            fee.getValue() == null ? null : Math.round(fee.getValue() * 100),
            includes.getValue(), durationMin.getValue(), city.getValue(), tpl);
        ListingDraft draft = studio.generateDraft(req);
        showDraft(preview, draft, category.getValue(), city.getValue(), location.getValue(),
            when.getValue(), durationMin.getValue(), slots.getValue());
      } catch (Exception ex) {
        Notification.show("Couldn't generate: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
      }
    });
    generate.getStyle().set("background", "var(--lumo-primary-color)").set("color", "white");

    root.add(idea, row1, row2, includes, templatePick, generate, preview);
    return root;
  }

  private void showDraft(Div preview, ListingDraft draft, WorkshopCategory category,
                         String city, String location, LocalDateTime when,
                         Integer durationMin, Integer slots) {
    preview.removeAll();
    VerticalLayout left = new VerticalLayout();
    left.setMaxWidth("420px");
    Image poster = new Image(draft.posterPreviewUrl(), "Listing poster");
    poster.setWidthFull();
    poster.getStyle().set("border-radius", "12px").set("box-shadow", "var(--lumo-box-shadow-m)");
    left.add(poster);

    VerticalLayout right = new VerticalLayout();
    right.setMaxWidth("520px");
    TextField title = new TextField("Title");
    title.setValue(draft.title());
    title.setWidthFull();
    TextArea desc = new TextArea("Description");
    desc.setValue(draft.description());
    desc.setWidthFull();
    desc.setHeight("120px");
    TextArea inc = new TextArea("What's included (one per line)");
    inc.setValue(String.join("\n", draft.includes()));
    inc.setWidthFull();
    NumberField price = new NumberField("Price per person ($)");
    price.setValue(draft.suggestedPriceCents() == null ? 0 : draft.suggestedPriceCents() / 100.0);
    Paragraph note = new Paragraph("Edit anything, then publish — or pick another poster style:");
    note.getStyle().set("color", "var(--lumo-secondary-text-color)");
    right.add(title, desc, inc, price, note);

    posters.templates().forEach(t -> {
      Button b = new Button(t.name(), e -> Notification.show("Re-render with " + t.id() + " via the API draft endpoint"));
      b.getStyle().set("border-radius", "999px");
      right.add(b);
    });

    Button publish = new Button("Publish listing 🚀", e -> {
      try {
        var pub = new app.discovery.web.dto.StudioDtos.PublishRequest(
            title.getValue(), desc.getValue(),
            List.of(inc.getValue().split("\n")), draft.templateId(), draft.posterPreviewUrl(),
            Math.round(price.getValue() * 100),
            category == null ? WorkshopCategory.OTHER : category,
            city, location,
            when == null ? List.of() : List.of(new app.discovery.web.dto.StudioDtos.SessionSpec(
                when.atZone(ZoneId.systemDefault()).toInstant(),
                when.plusMinutes(durationMin == null ? 120 : durationMin).atZone(ZoneId.systemDefault()).toInstant(),
                slots == null ? 10 : slots)));
        // reuse the service directly for the UI path
        var svcDraft = new ListingStudioService.ListingDraft(
            pub.title(), pub.description(), pub.includes(), pub.templateId(),
            List.of(), pub.posterPreviewUrl(), pub.suggestedPriceCents(), null, null);
        var sessions = pub.sessions().stream()
            .map(s -> new ListingStudioService.SessionSpec(s.startsAt(), s.endsAt(), s.capacity()))
            .toList();
        User me = session.currentUser();
        var w = studio.publish(me.getId(), svcDraft, pub.category(), pub.city(), pub.locationName(), sessions);
        Notification.show("Published! Listing #" + w.getId(), 4000, Notification.Position.MIDDLE);
        com.vaadin.flow.component.UI.getCurrent().navigate("workshop/" + w.getId());
      } catch (Exception ex) {
        Notification.show("Publish needs: " + ex.getMessage(), 6000, Notification.Position.MIDDLE);
      }
    });
    publish.getStyle().set("background", "var(--lumo-success-color)").set("color", "white");
    right.add(publish);

    preview.add(left, right);
  }

  private void refreshMine(VerticalLayout mine) {
    // placeholder hook: real list wired via service in next pass
    mine.add(new Paragraph("Your published workshops appear in Discover once live."));
  }
}
