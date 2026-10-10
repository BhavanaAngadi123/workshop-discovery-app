package app.discovery.ui;

import app.discovery.domain.User;
import app.discovery.service.BookingService;
import app.discovery.service.FeedService;
import app.discovery.service.PaymentService;
import app.discovery.service.ReviewService;
import app.discovery.service.WorkshopService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Route(value = "workshop", layout = MainLayout.class)
@PageTitle("Workshop")
@AnonymousAllowed
public class WorkshopDetailView extends VerticalLayout implements HasUrlParameter<Long> {
  private final WorkshopService workshops;
  private final BookingService bookings;
  private final PaymentService payments;
  private final ReviewService reviews;
  private final FeedService feed;
  private final SessionUtil session;
  private final VerticalLayout content = new VerticalLayout();

  private static final DateTimeFormatter FMT =
      DateTimeFormatter.ofPattern("EEE, MMM d · h:mm a").withZone(ZoneId.systemDefault());

  public WorkshopDetailView(WorkshopService workshops, BookingService bookings,
                            PaymentService payments, ReviewService reviews,
                            FeedService feed, SessionUtil session) {
    this.workshops = workshops;
    this.bookings = bookings;
    this.payments = payments;
    this.reviews = reviews;
    this.feed = feed;
    this.session = session;
    add(content);
  }

  @Override
  public void setParameter(BeforeEvent event, Long id) {
    content.removeAll();
    app.discovery.domain.Workshop w;
    try {
      w = workshops.getPublished(id);
    } catch (Exception e) {
      content.add(new H3("Workshop not found"));
      return;
    }
    String img = w.getPosterUrl() != null ? w.getPosterUrl() : w.getCoverPhotoUrl();
    if (img != null) {
      Image cover = new Image(img, w.getTitle());
      cover.setWidthFull();
      cover.setMaxHeight("420px");
      cover.getStyle().set("object-fit", "cover").set("border-radius", "16px");
      content.add(cover);
    }
    H2 title = new H2(w.getTitle());
    Paragraph meta = new Paragraph(String.format("★ %.1f (%d reviews) · %s · %s",
        w.getRatingAvg(), w.getRatingCount(), w.getCategory().name().toLowerCase().replace('_', ' '),
        w.getCity() == null ? "" : w.getCity()));
    H3 price = new H3(w.getPriceCents() == 0 ? "Free" : "$" + w.getPriceCents() / 100 + " per person");
    Paragraph desc = new Paragraph(w.getDescription() == null ? "" : w.getDescription());
    content.add(title, meta, price, desc);

    // always-visible founding fields
    Div facts = new Div();
    facts.getStyle().set("background", "var(--lumo-contrast-5pct)").set("border-radius", "12px").set("padding", "12px 16px");
    H4 factsTitle = new H4("The essentials");
    Paragraph loc = new Paragraph("📍 " + (w.getLocationName() == null ? w.getCity() : w.getLocationName() + ", " + w.getCity()));
    Div includes = new Div(new H5("What's included"));
    if (w.getIncludes() != null) {
      for (String line : w.getIncludes().split("\n")) {
        if (!line.isBlank()) includes.add(new Paragraph("✓ " + line.trim()));
      }
    }
    facts.add(factsTitle, loc, includes);
    content.add(facts);

    // sessions with timings + durations
    H4 sessionsTitle = new H4("Pick a time");
    content.add(sessionsTitle);
    var sessions = workshops.upcomingSessions(id);
    if (sessions.isEmpty()) content.add(new Paragraph("No upcoming sessions right now."));
    User me = session.currentUser();
    for (var s : sessions) {
      long mins = java.time.Duration.between(s.getStartsAt(), s.getEndsAt()).toMinutes();
      String label = String.format("%s · %dh %02dm · %d seats left",
          FMT.format(s.getStartsAt()), mins / 60, mins % 60, s.seatsLeft());
      HorizontalLayout row = new HorizontalLayout(new Span(label));
      row.setAlignItems(Alignment.CENTER);
      if (me != null && s.seatsLeft() > 0) {
        IntegerField seats = new IntegerField();
        seats.setMin(1);
        seats.setMax(s.seatsLeft());
        seats.setValue(1);
        seats.setWidth("70px");
        Button book = new Button("Book", e -> {
          try {
            var b = bookings.reserve(me.getId(), s.getId(), seats.getValue());
            var intent = payments.createIntent(me.getId(), b.getId());
            if (intent.live()) {
              Notification.show("Complete payment with the client secret in your Stripe dashboard (test mode).");
            } else {
              payments.simulateSuccess(intent.paymentIntentId());
              Notification.show("Booked! (test payment simulated)", 4000, Notification.Position.MIDDLE);
            }
          } catch (Exception ex) {
            Notification.show(ex.getMessage(), 4000, Notification.Position.MIDDLE);
          }
        });
        row.add(seats, book);
      }
      content.add(row);
    }

    // reviews
    H4 revTitle = new H4("Reviews");
    content.add(revTitle);
    reviews.forWorkshop(id, org.springframework.data.domain.PageRequest.of(0, 5)).forEach(r ->
        content.add(new Paragraph("★".repeat(r.getRating()) + " — " + r.getText())));
  }
}
