package app.discovery.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BookingDtos {
  public record ReserveRequest(@NotNull Long sessionId, @Min(1) int seats) {}

  public record BookingDto(Long id, Long sessionId, String workshopTitle, int seats,
                           long totalCents, String currency, String status,
                           String paymentIntentId, String startsAt) {}
}
