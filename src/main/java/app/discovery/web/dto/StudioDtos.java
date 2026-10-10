package app.discovery.web.dto;

import app.discovery.domain.WorkshopCategory;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

public class StudioDtos {
  public record DraftRequest(@NotBlank String ideaText, String categoryHint, String dateHint,
                             Integer slots, Long feeCents, String includesHint,
                             Integer durationMinutes, String city, String templateId) {}

  public record PublishRequest(
      String title, String description, List<String> includes, String templateId,
      String posterPreviewUrl, Long suggestedPriceCents, WorkshopCategory category,
      String city, String locationName, List<SessionSpec> sessions) {}

  public record SessionSpec(Instant startsAt, Instant endsAt, int capacity) {}

  public record TemplateDto(String id, String name, String blurb) {}
}
