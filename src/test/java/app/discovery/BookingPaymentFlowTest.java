package app.discovery;

import app.discovery.domain.BookingStatus;
import app.discovery.domain.Role;
import app.discovery.domain.Workshop;
import app.discovery.service.AuthService;
import app.discovery.service.BookingService;
import app.discovery.service.PaymentService;
import app.discovery.service.WorkshopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BookingPaymentFlowTest {
  @Autowired AuthService auth;
  @Autowired WorkshopService workshops;
  @Autowired BookingService bookings;
  @Autowired PaymentService payments;

  @Test
  void reservePayConfirm() throws Exception {
    var host = auth.register("pay-host@example.com", "password123", "Pay Host", Role.HOST);
    var customer = auth.register("pay-cust@example.com", "password123", "Pay Customer", Role.CUSTOMER);

    Workshop w = new Workshop();
    w.setTitle("Pasta Night");
    w.setPriceCents(3000);
    w.setLocationName("Kitchen Studio");
    w.setCity("Cambridge");
    w.setIncludes("Ingredients\nDinner");
    var draft = workshops.createDraft(host.userId(), w);
    var s = workshops.addSession(host.userId(), draft.getId(),
        Instant.now().plus(3, ChronoUnit.DAYS),
        Instant.now().plus(3, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS), 5);
    workshops.publish(host.userId(), draft.getId());

    var b = bookings.reserve(customer.userId(), s.getId(), 2);
    assertEquals(BookingStatus.PENDING_PAYMENT, b.getStatus());
    assertEquals(6000, b.getTotalCents());

    var intent = payments.createIntent(customer.userId(), b.getId());
    assertNotNull(intent.paymentIntentId());
    assertFalse(intent.live()); // test mode: Stripe disabled

    payments.simulateSuccess(intent.paymentIntentId());

    var mine = bookings.myBookings(customer.userId());
    assertEquals(1, mine.size());
    assertEquals(BookingStatus.CONFIRMED, mine.get(0).getStatus());
  }
}
