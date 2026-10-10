package app.discovery.service;

import app.discovery.domain.*;
import app.discovery.repo.BookingRepository;
import app.discovery.repo.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Stripe payments in TEST mode. No real money moves until live keys are configured.
 * Swap test keys for live keys in env to go live — no code changes needed.
 */
@Service
public class PaymentService {
  private final BookingRepository bookings;
  private final PaymentRepository payments;
  private final BookingService bookingService;
  private final AuditService audit;

  @Value("${app.stripe.secret-key:}")
  private String stripeSecretKey;

  @Value("${app.stripe.webhook-secret:}")
  private String webhookSecret;

  @Value("${app.stripe.enabled:false}")
  private boolean stripeEnabled;

  public PaymentService(BookingRepository bookings, PaymentRepository payments,
                       BookingService bookingService, AuditService audit) {
    this.bookings = bookings;
    this.payments = payments;
    this.bookingService = bookingService;
    this.audit = audit;
  }

  @PostConstruct
  void init() {
    if (stripeEnabled && stripeSecretKey != null && !stripeSecretKey.isBlank()) {
      Stripe.apiKey = stripeSecretKey;
    }
  }

  public record IntentResult(String clientSecret, String paymentIntentId, long amountCents, boolean live) {}

  /** Creates a Stripe PaymentIntent for a PENDING_PAYMENT booking. */
  @Transactional
  public IntentResult createIntent(Long customerId, Long bookingId) throws StripeException {
    Booking b = bookings.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    if (!b.getCustomer().getId().equals(customerId)) throw new SecurityException("Not your booking");
    if (b.getStatus() != BookingStatus.PENDING_PAYMENT) throw new IllegalArgumentException("Booking not payable");

    String piId;
    String clientSecret;
    if (stripeEnabled) {
      Map<String, String> metadata = new HashMap<>();
      metadata.put("booking_id", String.valueOf(b.getId()));
      PaymentIntent intent = PaymentIntent.create(PaymentIntentCreateParams.builder()
          .setAmount(b.getTotalCents())
          .setCurrency(b.getCurrency().toLowerCase())
          .putAllMetadata(metadata)
          .setAutomaticPaymentMethods(
              PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
          .build());
      piId = intent.getId();
      clientSecret = intent.getClientSecret();
    } else {
      // Local simulation for development without Stripe keys: deterministic test-mode intent.
      piId = "pi_test_" + b.getId() + "_" + System.currentTimeMillis();
      clientSecret = piId + "_secret_test";
    }

    Payment p = new Payment();
    p.setBooking(b);
    p.setStripePaymentIntentId(piId);
    p.setAmountCents(b.getTotalCents());
    p.setCurrency(b.getCurrency());
    p.setStatus(PaymentStatus.PROCESSING);
    payments.save(p);
    b.setStripePaymentIntentId(piId);
    audit.log(customerId, "PAYMENT_INTENT", "Payment", p.getId());
    return new IntentResult(clientSecret, piId, b.getTotalCents(), stripeEnabled);
  }

  /** Stripe webhook handler — verify signature when a webhook secret is configured. */
  @Transactional
  public void handleWebhook(String payload, String sigHeader) {
    if (stripeEnabled && webhookSecret != null && !webhookSecret.isBlank()) {
      try {
        var event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        String piId = extractPaymentIntentId(event);
        if (piId != null) applyEvent(event.getType(), piId);
      } catch (Exception e) {
        throw new IllegalArgumentException("Invalid webhook signature");
      }
    } else {
      // Dev path: accept a simple JSON {type, payment_intent_id} for local testing.
      try {
        var node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(payload);
        applyEvent(node.path("type").asText(), node.path("payment_intent_id").asText());
      } catch (Exception e) {
        throw new IllegalArgumentException("Bad webhook payload");
      }
    }
  }

  private String extractPaymentIntentId(com.stripe.model.Event event) {
    try {
      var opt = event.getDataObjectDeserializer().getObject();
      if (opt.isEmpty()) return null;
      var obj = opt.get();
      if (obj instanceof PaymentIntent pi) return pi.getId();
      if (obj instanceof Refund r && r.getPaymentIntent() != null) return r.getPaymentIntent();
    } catch (Exception ignored) {}
    return null;
  }

  private void applyEvent(String type, String piId) {
    if (piId == null || piId.isBlank()) return;
    payments.findByStripePaymentIntentId(piId).ifPresent(p -> {
      switch (type) {
        case "payment_intent.succeeded" -> {
          p.setStatus(PaymentStatus.SUCCEEDED);
          bookingService.confirmPaid(p.getBooking().getId(), piId);
          audit.log(p.getBooking().getCustomer().getId(), "PAYMENT_SUCCEEDED", "Payment", p.getId());
        }
        case "payment_intent.payment_failed", "payment_intent.canceled" -> {
          p.setStatus(PaymentStatus.FAILED);
          audit.log(p.getBooking().getCustomer().getId(), "PAYMENT_FAILED", "Payment", p.getId());
        }
        case "charge.refunded" -> {
          p.setStatus(PaymentStatus.REFUNDED);
          Booking b = p.getBooking();
          b.setStatus(BookingStatus.REFUNDED);
          audit.log(b.getCustomer().getId(), "PAYMENT_REFUNDED", "Payment", p.getId());
        }
        default -> { /* ignore other event types */ }
      }
    });
  }

  /** Dev-only helper: simulate a successful test payment without Stripe. */
  @Transactional
  public void simulateSuccess(String paymentIntentId) {
    if (stripeEnabled) throw new IllegalStateException("Simulation disabled when Stripe is live");
    applyEvent("payment_intent.succeeded", paymentIntentId);
  }
}
