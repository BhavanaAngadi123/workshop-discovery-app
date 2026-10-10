package app.discovery.service;

import app.discovery.domain.*;
import app.discovery.repo.HostProfileRepository;
import app.discovery.repo.UserRepository;
import app.discovery.repo.WorkshopRepository;
import app.discovery.repo.WorkshopSessionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class WorkshopService {
  private final WorkshopRepository workshops;
  private final WorkshopSessionRepository sessions;
  private final UserRepository users;
  private final HostProfileRepository hosts;
  private final AuditService audit;

  public WorkshopService(WorkshopRepository workshops, WorkshopSessionRepository sessions,
                         UserRepository users, HostProfileRepository hosts, AuditService audit) {
    this.workshops = workshops;
    this.sessions = sessions;
    this.users = users;
    this.hosts = hosts;
    this.audit = audit;
  }

  public Page<Workshop> search(WorkshopCategory category, String city, String q,
                              Long maxPriceCents, String seasonSlug, Pageable pageable) {
    return workshops.search(category, city, q, maxPriceCents, seasonSlug, pageable);
  }

  public Workshop getPublished(Long id) {
    Workshop w = workshops.findById(id).orElseThrow(() -> new IllegalArgumentException("Workshop not found"));
    if (w.getStatus() != WorkshopStatus.PUBLISHED) throw new IllegalArgumentException("Workshop not found");
    return w;
  }

  @Transactional
  public Workshop createDraft(Long hostId, Workshop w) {
    User host = users.findById(hostId).orElseThrow(() -> new IllegalArgumentException("Host not found"));
    w.setHost(host);
    w.setStatus(WorkshopStatus.DRAFT);
    workshops.save(w);
    audit.log(hostId, "WORKSHOP_DRAFT", "Workshop", w.getId());
    return w;
  }

  /**
   * Founding principle: no listing goes live without price, timings, location,
   * and what's-included — always visible, never "contact for price".
   */
  @Transactional
  public Workshop publish(Long hostId, Long workshopId) {
    Workshop w = loadOwned(hostId, workshopId);
    if (w.getTitle() == null || w.getTitle().isBlank()) throw new IllegalArgumentException("Title required");
    if (w.getPriceCents() < 0) throw new IllegalArgumentException("Price must be >= 0 (0 = free)");
    if ((w.getLocationName() == null || w.getLocationName().isBlank())
        && (w.getCity() == null || w.getCity().isBlank()))
      throw new IllegalArgumentException("Location is required — listings must show where");
    if (w.getIncludes() == null || w.getIncludes().isBlank())
      throw new IllegalArgumentException("What's-included is required — listings must show what's included");
    var upcoming = sessions.upcomingForWorkshop(w.getId(), Instant.now());
    if (upcoming.isEmpty())
      throw new IllegalArgumentException("At least one scheduled session with date/time is required");
    w.setStatus(WorkshopStatus.PUBLISHED);
    audit.log(hostId, "WORKSHOP_PUBLISH", "Workshop", w.getId());
    return w;
  }

  @Transactional
  public Workshop update(Long hostId, Long workshopId, Workshop patch) {
    Workshop w = loadOwned(hostId, workshopId);
    if (patch.getTitle() != null) w.setTitle(patch.getTitle());
    if (patch.getDescription() != null) w.setDescription(patch.getDescription());
    if (patch.getCategory() != null) w.setCategory(patch.getCategory());
    if (patch.getPriceCents() >= 0) w.setPriceCents(patch.getPriceCents());
    if (patch.getLocationName() != null) w.setLocationName(patch.getLocationName());
    if (patch.getCity() != null) w.setCity(patch.getCity());
    if (patch.getCoverPhotoUrl() != null) w.setCoverPhotoUrl(patch.getCoverPhotoUrl());
    if (patch.getPosterUrl() != null) w.setPosterUrl(patch.getPosterUrl());
    if (patch.getIncludes() != null) w.setIncludes(patch.getIncludes());
    if (patch.getSeasonSlug() != null) w.setSeasonSlug(patch.getSeasonSlug());
    if (patch.getPhotos() != null && !patch.getPhotos().isEmpty()) {
      w.getPhotos().clear();
      patch.getPhotos().forEach(p -> { p.setWorkshop(w); w.getPhotos().add(p); });
    }
    audit.log(hostId, "WORKSHOP_UPDATE", "Workshop", w.getId());
    return w;
  }

  @Transactional
  public WorkshopSession addSession(Long hostId, Long workshopId, Instant startsAt, Instant endsAt, int capacity) {
    Workshop w = loadOwned(hostId, workshopId);
    if (!startsAt.isBefore(endsAt)) throw new IllegalArgumentException("Session must end after it starts");
    if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
    WorkshopSession s = new WorkshopSession();
    s.setWorkshop(w);
    s.setStartsAt(startsAt);
    s.setEndsAt(endsAt);
    s.setCapacity(capacity);
    sessions.save(s);
    audit.log(hostId, "SESSION_ADD", "WorkshopSession", s.getId());
    return s;
  }

  public List<WorkshopSession> upcomingSessions(Long workshopId) {
    return sessions.upcomingForWorkshop(workshopId, Instant.now());
  }

  private Workshop loadOwned(Long hostId, Long workshopId) {
    Workshop w = workshops.findById(workshopId).orElseThrow(() -> new IllegalArgumentException("Workshop not found"));
    if (!w.getHost().getId().equals(hostId)) throw new SecurityException("Not your workshop");
    return w;
  }
}
