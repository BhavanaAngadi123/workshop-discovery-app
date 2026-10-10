package app.discovery.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {
  public MainLayout() {
    DrawerToggle toggle = new DrawerToggle();
    H1 title = new H1("Workshop Discovery");
    title.getStyle().set("font-size", "1.3em").set("margin", "0");
    HorizontalLayout header = new HorizontalLayout(toggle, title);
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.setWidthFull();
    addToNavbar(header);

    Tabs tabs = new Tabs();
    tabs.setOrientation(Tabs.Orientation.VERTICAL);
    tabs.add(
        tab("Discover", DiscoverView.class),
        tab("Experience Feed", FeedView.class),
        tab("AI Concierge", ConciergeView.class),
        tab("Host Studio", HostDashboardView.class));
    Span hint = new Span("Find it. Book it. Share it.");
    hint.getStyle().set("font-size", "0.8em").set("color", "var(--lumo-secondary-text-color)")
        .set("padding", "0 16px");
    com.vaadin.flow.component.orderedlayout.VerticalLayout drawer =
        new com.vaadin.flow.component.orderedlayout.VerticalLayout(tabs, hint);
    addToDrawer(drawer);
  }

  private Tab tab(String label, Class<? extends com.vaadin.flow.component.Component> target) {
    Tab tab = new Tab();
    tab.add(new RouterLink(label, target));
    return tab;
  }
}
