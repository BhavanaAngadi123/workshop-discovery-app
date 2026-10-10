package app.discovery.web;

import app.discovery.domain.User;
import app.discovery.service.BookingService;
import app.discovery.web.dto.BookingDtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings")
public class BookingController {
  private final BookingService bookings;

  public BookingController(BookingService bookings) {
    this.bookings = bookings;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Reserve seats (payment completes the booking)")
  public BookingDto reserve(@AuthenticationPrincipal User me, @Valid @RequestBody ReserveRequest req) {
    var b = bookings.reserve(me.getId(), req.sessionId(), req.seats());
    return toDto(b);
  }

  @GetMapping("/mine")
  @Operation(summary = "My bookings")
  public List<BookingDto> mine(@AuthenticationPrincipal User me) {
    return bookings.myBookings(me.getId()).stream().map(this::toDto).toList();
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Cancel my booking")
  public void cancel(@AuthenticationPrincipal User me, @PathVariable Long id) {
    bookings.cancel(me.getId(), id);
  }

  private BookingDto toDto(app.discovery.domain.Booking b) {
    return new BookingDto(b.getId(), b.getSession().getId(),
        b.getSession().getWorkshop().getTitle(), b.getSeats(), b.getTotalCents(),
        b.getCurrency(), b.getStatus().name(), b.getStripePaymentIntentId(),
        b.getSession().getStartsAt().toString());
  }
}
