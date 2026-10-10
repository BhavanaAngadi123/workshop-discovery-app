package app.discovery.web;

import app.discovery.domain.User;
import app.discovery.domain.Workshop;
import app.discovery.domain.WorkshopCategory;
import app.discovery.domain.WorkshopPhoto;
import app.discovery.service.WorkshopService;
import app.discovery.web.dto.WorkshopDtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@Tag(name = "Workshops")
public class WorkshopController {
  private final WorkshopService workshops;

  public WorkshopController(WorkshopService workshops) {
    this.workshops = workshops;
  }

  @GetMapping("/api/workshops")
  @Operation(summary = "Search published workshops: category, city, q, maxPriceCents, seasonSlug")
  public Page<WorkshopSummary> search(
      @RequestParam(required = false) WorkshopCategory category,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Long maxPriceCents,
      @RequestParam(required = false) String seasonSlug,
      Pageable pageable) {
    return workshops.search(category, city, q, maxPriceCents, seasonSlug, pageable)
        .map(WorkshopSummary::of);
  }

  @GetMapping("/api/workshops/{id}")
  @Operation(summary = "Workshop detail: price, timings, location, what's-included always visible")
  public WorkshopDetail detail(@PathVariable Long id) {
    Workshop w = workshops.getPublished(id);
    List<SessionDto> sessions = workshops.upcomingSessions(id).stream()
        .map(s -> new SessionDto(s.getId(), s.getStartsAt(), s.getEndsAt(), s.getCapacity(),
            s.getBookedSeats(), s.seatsLeft(), s.getStatus().name(),
            Duration.between(s.getStartsAt(), s.getEndsAt()).toMinutes()))
        .toList();
    List<String> photos = w.getPhotos().stream().map(WorkshopPhoto::getUrl).toList();
    return new WorkshopDetail(WorkshopSummary.of(w), w.getDescription(), w.getLocationName(),
        w.getLatitude(), w.getLongitude(), w.getIncludes(), photos, sessions,
        w.getHost().getDisplayName(), false);
  }

  @PostMapping("/api/host/workshops")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('HOST','ADMIN')")
  @Operation(summary = "Host: create draft listing (manual path — AI Studio optional)")
  public WorkshopSummary create(@AuthenticationPrincipal User me, @Valid @RequestBody CreateRequest req) {
    Workshop w = new Workshop();
    w.setTitle(req.title());
    w.setDescription(req.description());
    w.setCategory(req.category() == null ? WorkshopCategory.OTHER : req.category());
    w.setPriceCents(req.priceCents());
    if (req.currency() != null) w.setCurrency(req.currency());
    w.setLocationName(req.locationName());
    w.setCity(req.city());
    w.setLatitude(req.latitude());
    w.setLongitude(req.longitude());
    w.setCoverPhotoUrl(req.coverPhotoUrl());
    w.setIncludes(req.includes());
    w.setSeasonSlug(req.seasonSlug());
    if (req.photoUrls() != null) {
      int i = 0;
      for (String url : req.photoUrls()) {
        WorkshopPhoto p = new WorkshopPhoto();
        p.setWorkshop(w);
        p.setUrl(url);
        p.setSortOrder(i++);
        w.getPhotos().add(p);
      }
    }
    return WorkshopSummary.of(workshops.createDraft(me.getId(), w));
  }

  @PutMapping("/api/host/workshops/{id}")
  @PreAuthorize("hasAnyRole('HOST','ADMIN')")
  @Operation(summary = "Host: update own listing")
  public WorkshopSummary update(@AuthenticationPrincipal User me, @PathVariable Long id,
                                @RequestBody CreateRequest req) {
    Workshop patch = new Workshop();
    patch.setTitle(req.title());
    patch.setDescription(req.description());
    patch.setCategory(req.category());
    patch.setPriceCents(req.priceCents());
    patch.setLocationName(req.locationName());
    patch.setCity(req.city());
    patch.setCoverPhotoUrl(req.coverPhotoUrl());
    patch.setIncludes(req.includes());
    patch.setSeasonSlug(req.seasonSlug());
    return WorkshopSummary.of(workshops.update(me.getId(), id, patch));
  }

  @PostMapping("/api/host/workshops/{id}/publish")
  @PreAuthorize("hasAnyRole('HOST','ADMIN')")
  @Operation(summary = "Host: publish — requires price, timings, location, what's-included")
  public WorkshopSummary publish(@AuthenticationPrincipal User me, @PathVariable Long id) {
    return WorkshopSummary.of(workshops.publish(me.getId(), id));
  }

  @PostMapping("/api/host/workshops/{id}/sessions")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('HOST','ADMIN')")
  @Operation(summary = "Host: add a session (any duration — 1hr, 2hr, full-day, multi-day)")
  public SessionDto addSession(@AuthenticationPrincipal User me, @PathVariable Long id,
                              @Valid @RequestBody SessionRequest req) {
    var s = workshops.addSession(me.getId(), id, req.startsAt(), req.endsAt(), req.capacity());
    return new SessionDto(s.getId(), s.getStartsAt(), s.getEndsAt(), s.getCapacity(),
        s.getBookedSeats(), s.seatsLeft(), s.getStatus().name(),
        Duration.between(s.getStartsAt(), s.getEndsAt()).toMinutes());
  }

  @GetMapping("/api/host/workshops/{id}/sessions")
  @PreAuthorize("hasAnyRole('HOST','ADMIN')")
  public List<SessionDto> sessions(@PathVariable Long id) {
    return workshops.upcomingSessions(id).stream()
        .map(s -> new SessionDto(s.getId(), s.getStartsAt(), s.getEndsAt(), s.getCapacity(),
            s.getBookedSeats(), s.seatsLeft(), s.getStatus().name(),
            Duration.between(s.getStartsAt(), s.getEndsAt()).toMinutes()))
        .toList();
  }
}
