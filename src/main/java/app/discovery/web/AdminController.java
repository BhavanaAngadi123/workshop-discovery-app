package app.discovery.web;

import app.discovery.domain.HostProfile;
import app.discovery.domain.User;
import app.discovery.repo.AuditLogRepository;
import app.discovery.repo.HostProfileRepository;
import app.discovery.repo.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
  private final HostProfileRepository hosts;
  private final UserRepository users;
  private final AuditLogRepository auditLog;

  public AdminController(HostProfileRepository hosts, UserRepository users, AuditLogRepository auditLog) {
    this.hosts = hosts;
    this.users = users;
    this.auditLog = auditLog;
  }

  @PostMapping("/hosts/{userId}/verify")
  @Operation(summary = "Verify a host")
  public Map<String, Object> verifyHost(@AuthenticationPrincipal User me, @PathVariable Long userId) {
    HostProfile hp = hosts.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Host not found"));
    hp.setVerified(true);
    auditLog.save(new app.discovery.domain.AuditLog(me.getId(), "HOST_VERIFY", "HostProfile", hp.getId()));
    return Map.of("userId", userId, "verified", true);
  }

  @GetMapping("/audit")
  @Operation(summary = "Audit log (sensitive actions)")
  public Page<Map<String, Object>> audit(Pageable pageable) {
    return auditLog.findAll(pageable).map(a -> Map.<String, Object>of(
        "id", a.getId(), "actorId", a.getActorId() == null ? "" : a.getActorId(),
        "action", a.getAction(), "entityType", a.getEntityType() == null ? "" : a.getEntityType(),
        "entityId", a.getEntityId() == null ? "" : a.getEntityId(),
        "createdAt", a.getCreatedAt().toString()));
  }
}
