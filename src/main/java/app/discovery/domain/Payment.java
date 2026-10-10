package app.discovery.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "booking_id", unique = true)
  private Booking booking;

  @Column(name = "stripe_payment_intent_id", nullable = false, unique = true, length = 120)
  private String stripePaymentIntentId;

  @Column(name = "amount_cents", nullable = false)
  private long amountCents;

  @Column(nullable = false, length = 3)
  private String currency = "USD";

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private PaymentStatus status = PaymentStatus.REQUIRES_PAYMENT;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  public Long getId() { return id; }
  public Booking getBooking() { return booking; }
  public void setBooking(Booking booking) { this.booking = booking; }
  public String getStripePaymentIntentId() { return stripePaymentIntentId; }
  public void setStripePaymentIntentId(String id) { this.stripePaymentIntentId = id; }
  public long getAmountCents() { return amountCents; }
  public void setAmountCents(long amountCents) { this.amountCents = amountCents; }
  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
  public PaymentStatus getStatus() { return status; }
  public void setStatus(PaymentStatus status) { this.status = status; }
  public Instant getCreatedAt() { return createdAt; }
}
