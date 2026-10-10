package app.discovery.web.dto;

import app.discovery.domain.Workshop;
import app.discovery.domain.WorkshopCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

public class WorkshopDtos {
  public record CreateRequest(
      @NotBlank String title, String description, WorkshopCategory category,
      @Min(0) long priceCents, String currency,
      String locationName, String city, Double latitude, Double longitude,
      String coverPhotoUrl, String includes, String seasonSlug,
      List<String> photoUrls) {}

  public record SessionRequest(Instant startsAt, Instant endsAt, @Min(1) int capacity) {}

  public record WorkshopSummary(Long id, String title, WorkshopCategory category, long priceCents,
                                String currency, String city, String coverPhotoUrl, String posterUrl,
                                double ratingAvg, int ratingCount, String seasonSlug) {
    public static WorkshopSummary of(Workshop w) {
      return new WorkshopSummary(w.getId(), w.getTitle(), w.getCategory(), w.getPriceCents(),
          w.getCurrency(), w.getCity(), w.getCoverPhotoUrl(), w.getPosterUrl(),
          w.getRatingAvg(), w.getRatingCount(), w.getSeasonSlug());
    }
  }

  public record SessionDto(Long id, Instant startsAt, Instant endsAt, int capacity,
                           int bookedSeats, int seatsLeft, String status, long durationMinutes) {}

  public record WorkshopDetail(WorkshopSummary summary, String description, String locationName,
                               Double latitude, Double longitude, String includes,
                               List<String> photoUrls, List<SessionDto> upcomingSessions,
                               String hostName, boolean hostVerified) {}
}
