package app.discovery.service;

import app.discovery.domain.*;
import app.discovery.repo.BookingRepository;
import app.discovery.repo.UserRepository;
import app.discovery.repo.WorkshopSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class BookingService {
  private final BookingRepository bookings;
  private final WorkshopSessionRepository sessions;
  private final UserRepository users;
  private final AuditService audit;

  public BookingService(BookingRepository bookings, WorkshopSessionRepository sessions,
                       UserRepository users, AuditService audit) {
    this.bookings = bookings;
    this.sessions = sessions;
    this.users = users;
    this.audit = audit;
  }

  /** Reserves seats; payment completes the booking. Synchronized on session to avoid oversell. */
  @Transactional
  public Booking reserve(Long customerId, Long sessionId, int seats) {
    if (seats <= 0) throw new IllegalArgumentException("Seats must be positive");
    WorkshopSession s = sessions.findById(sessionId)
        .orElseThrow(() -> new IllegalArgumentException("Session not found"));
    synchronized (sessionLock(s.getId())) {
      WorkshopSession fresh = sessions.findById(sessionId).orElseThrow();
      if (fresh.getStatus() != SessionStatus.SCHEDULED) throw new IllegalArgumentException("Session not bookable");
      if (fresh.getStartsAt().isBefore(Instant.now())) throw new IllegalArgumentException("Session already started");
      if (fresh.seatsLeft() < seats) throw new IllegalArgumentException("Only " + fresh.seatsLeft() + " seats left");
      fresh.setBookedSeats(fresh.getBookedSeats() + seats);

      User customer = users.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
      Booking b = new Booking();
      b.setCustomer(customer);
      b.setSession(fresh);
      b.setSeats(seats);
      b.setTotalCents(fresh.getWorkshop().getPriceCents() * seats);
      b.setCurrency(fresh.getWorkshop().getCurrency());
      b.setStatus(BookingStatus.PENDING_PAYMENT);
      bookings.save(b);
      audit.log(customerId, "BOOKING_RESERVE", "Booking", b.getId());
      return b;
    }
  }

  @Transactional
  public void confirmPaid(Long bookingId, String paymentIntentId) {
    Booking b = bookings.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    b.setStatus(BookingStatus.CONFIRMED);
    b.setStripePaymentIntentId(paymentIntentId);
    audit.log(b.getCustomer().getId(), "BOOKING_CONFIRM", "Booking", b.getId());
  }

  @Transactional
  public void cancel(Long customerId, Long bookingId) {
    Booking b = bookings.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    if (!b.getCustomer().getId().equals(customerId)) throw new SecurityException("Not your booking");
    if (b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING_PAYMENT) {
      b.setStatus(BookingStatus.CANCELLED);
      WorkshopSession s = b.getSession();
      s.setBookedSeats(Math.max(0, s.getBookedSeats() - b.getSeats()));
      audit.log(customerId, "BOOKING_CANCEL", "Booking", b.getId());
    }
  }

  public List<Booking> myBookings(Long customerId) {
    return bookings.findByCustomerIdOrderByCreatedAtDesc(customerId);
  }

  private final java.util.concurrent.ConcurrentHashMap<Long, Object> locks = new java.util.concurrent.ConcurrentHashMap<>();

  private Object sessionLock(Long sessionId) {
    return locks.computeIfAbsent(sessionId, k -> new Object());
  }
}
