package app.discovery.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "workshop_sessions")
public class WorkshopSession {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "workshop_id")
  private Workshop workshop;

  @Column(name = "starts_at", nullable = false)
  private Instant startsAt;

  @Column(name = "ends_at", nullable = false)
  private Instant endsAt;

  @Column(nullable = false)
  private int capacity;

  @Column(name = "booked_seats", nullable = false)
  private int bookedSeats = 0;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SessionStatus status = SessionStatus.SCHEDULED;

  public Long getId() { return id; }
  public Workshop getWorkshop() { return workshop; }
  public void setWorkshop(Workshop workshop) { this.workshop = workshop; }
  public Instant getStartsAt() { return startsAt; }
  public void setStartsAt(Instant startsAt) { this.startsAt = startsAt; }
  public Instant getEndsAt() { return endsAt; }
  public void setEndsAt(Instant endsAt) { this.endsAt = endsAt; }
  public int getCapacity() { return capacity; }
  public void setCapacity(int capacity) { this.capacity = capacity; }
  public int getBookedSeats() { return bookedSeats; }
  public void setBookedSeats(int bookedSeats) { this.bookedSeats = bookedSeats; }
  public SessionStatus getStatus() { return status; }
  public void setStatus(SessionStatus status) { this.status = status; }
  public int seatsLeft() { return Math.max(0, capacity - bookedSeats); }
}
