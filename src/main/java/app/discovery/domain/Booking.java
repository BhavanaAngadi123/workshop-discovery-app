package app.discovery.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id")
  private User customer;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "session_id")
  private WorkshopSession session;

  @Column(nullable = false)
  private int seats;

  @Column(name = "total_cents", nullable = false)
  private long totalCents;

  @Column(nullable = false, length = 3)
  private String currency = "USD";

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private BookingStatus status = BookingStatus.PENDING_PAYMENT;

  @Column(name = "stripe_payment_intent_id", length = 120)
  private String stripePaymentIntentId;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  public Long getId() { return id; }
  public User getCustomer() { return customer; }
  public void setCustomer(User customer) { this.customer = customer; }
  public WorkshopSession getSession() { return session; }
  public void setSession(WorkshopSession session) { this.session = session; }
  public int getSeats() { return seats; }
  public void setSeats(int seats) { this.seats = seats; }
  public long getTotalCents() { return totalCents; }
  public void setTotalCents(long totalCents) { this.totalCents = totalCents; }
  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
  public BookingStatus getStatus() { return status; }
  public void setStatus(BookingStatus status) { this.status = status; }
  public String getStripePaymentIntentId() { return stripePaymentIntentId; }
  public void setStripePaymentIntentId(String id) { this.stripePaymentIntentId = id; }
  public Instant getCreatedAt() { return createdAt; }
}
