package app.discovery.ui;

import app.discovery.domain.WorkshopCategory;
import app.discovery.service.WorkshopService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.data.domain.PageRequest;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Discover workshops")
@AnonymousAllowed
public class DiscoverView extends VerticalLayout {
  private final WorkshopService workshops;
  private final ComboBox<WorkshopCategory> category = new ComboBox<>("Category");
  private final TextField city = new TextField("City");
  private final TextField query = new TextField("Search");
  private final NumberField maxPrice = new NumberField("Max price ($)");
  private final FlexLayout cards = new FlexLayout();

  public DiscoverView(WorkshopService workshops) {
    this.workshops = workshops;
    setPadding(true);

    category.setItems(WorkshopCategory.values());
    category.setClearButtonVisible(true);
    query.setPlaceholder("pottery, pasta, pumpkin picking...");
    city.setPlaceholder("Boston");
    maxPrice.setMin(0);
    Button search = new Button("Search", e -> load());
    search.addClickListener(e -> load());

    com.vaadin.flow.component.orderedlayout.HorizontalLayout filters =
        new com.vaadin.flow.component.orderedlayout.HorizontalLayout(query, category, city, maxPrice, search);
    filters.setWrap(true);
    filters.setAlignItems(Alignment.END);

    cards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
    cards.getStyle().set("gap", "16px");

    add(new H3("Find your next favorite thing to do"), filters, cards);
    load();
  }

  private void load() {
    cards.removeAll();
    Long maxCents = maxPrice.getValue() == null ? null : Math.round(maxPrice.getValue() * 100);
    var page = workshops.search(category.getValue(),
        city.getValue().isBlank() ? null : city.getValue(),
        query.getValue().isBlank() ? null : query.getValue(),
        maxCents, null, PageRequest.of(0, 24));
    if (page.isEmpty()) {
      cards.add(new Span("Nothing found yet — try the AI Concierge, it loves vague ideas."));
      return;
    }
    page.forEach(w -> cards.add(card(w)));
  }

  private Div card(app.discovery.domain.Workshop w) {
    Div card = new Div();
    card.getStyle()
        .set("width", "280px").set("border-radius", "16px").set("overflow", "hidden")
        .set("background", "var(--lumo-base-color)")
        .set("box-shadow", "var(--lumo-box-shadow-s)").set("cursor", "pointer");
    String imgUrl = w.getPosterUrl() != null ? w.getPosterUrl()
        : (w.getCoverPhotoUrl() != null ? w.getCoverPhotoUrl() : null);
    if (imgUrl != null) {
      Image img = new Image(imgUrl, w.getTitle());
      img.setWidthFull();
      img.setHeight("180px");
      img.getStyle().set("object-fit", "cover");
      card.add(img);
    }
    Div body = new Div();
    body.getStyle().set("padding", "12px 14px");
    H3 title = new H3(w.getTitle());
    title.getStyle().set("margin", "0 0 4px").set("font-size", "1.05em");
    Span meta = new Span(String.format("$%d · %s · ★ %.1f (%d)",
        w.getPriceCents() / 100, w.getCity() == null ? "—" : w.getCity(),
        w.getRatingAvg(), w.getRatingCount()));
    meta.getStyle().set("color", "var(--lumo-secondary-text-color)").set("font-size", "0.85em");
    body.add(title, meta);
    card.add(body);
    card.addClickListener(e -> UI.getCurrent().navigate("workshop/" + w.getId()));
    return card;
  }
}
