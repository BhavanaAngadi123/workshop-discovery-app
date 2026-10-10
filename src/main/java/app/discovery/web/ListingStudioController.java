package app.discovery.web;

import app.discovery.domain.User;
import app.discovery.poster.PosterRenderer;
import app.discovery.service.ListingStudioService;
import app.discovery.service.ListingStudioService.ListingDraft;
import app.discovery.web.dto.StudioDtos.DraftRequest;
import app.discovery.web.dto.StudioDtos.PublishRequest;
import app.discovery.web.dto.StudioDtos.SessionSpec;
import app.discovery.web.dto.StudioDtos.TemplateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/host/listings")
@Tag(name = "AI Listing Studio")
@PreAuthorize("hasAnyRole('HOST','ADMIN')")
public class ListingStudioController {
  private final ListingStudioService studio;
  private final PosterRenderer posters;

  public ListingStudioController(ListingStudioService studio, PosterRenderer posters) {
    this.studio = studio;
    this.posters = posters;
  }

  @GetMapping("/templates")
  @Operation(summary = "Poster template options the AI can use (or the host can pick)")
  public List<TemplateDto> templates() {
    return posters.templates().stream()
        .map(t -> new TemplateDto(t.id(), t.name(), t.blurb()))
        .toList();
  }

  @PostMapping("/draft")
  @Operation(summary = "AI generates a full listing draft: copy + poster + booking setup, from rough words")
  public DraftDto draft(@Valid @RequestBody DraftRequest req) throws Exception {
    var in = new ListingStudioService.DraftRequest(
        req.ideaText(), req.categoryHint(), req.dateHint(), req.slots(), req.feeCents(),
        req.includesHint(), req.durationMinutes(), req.city(), req.templateId());
    ListingDraft d = studio.generateDraft(in);
    return new DraftDto(d.title(), d.description(), d.includes(), d.templateId(),
        d.templateOptions().stream().map(t -> new TemplateDto(t.id(), t.name(), t.blurb())).toList(),
        d.posterPreviewUrl(), d.suggestedPriceCents(), d.suggestedCapacity(), d.dateLine());
  }

  public record DraftDto(String title, String description, java.util.List<String> includes,
                         String templateId, java.util.List<TemplateDto> templateOptions,
                         String posterPreviewUrl, Long suggestedPriceCents,
                         Integer suggestedCapacity, String dateLine) {}

  @PostMapping("/publish")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Publish an AI draft (host-edited or as-is) as a live listing")
  public java.util.Map<String, Object> publish(@AuthenticationPrincipal User me,
                                               @Valid @RequestBody PublishRequest req) {
    var draft = new ListingDraft(req.title(), req.description(),
        req.includes() == null ? List.of() : req.includes(),
        req.templateId(), List.of(), req.posterPreviewUrl(),
        req.suggestedPriceCents(), null, null);
    var sessions = req.sessions() == null ? List.<ListingStudioService.SessionSpec>of() : req.sessions().stream()
        .map(s -> new ListingStudioService.SessionSpec(s.startsAt(), s.endsAt(), s.capacity()))
        .toList();
    var w = studio.publish(me.getId(), draft, req.category(), req.city(), req.locationName(), sessions);
    return java.util.Map.of("workshopId", w.getId(), "posterUrl", w.getPosterUrl());
  }
}
