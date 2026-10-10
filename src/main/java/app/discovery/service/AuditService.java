package app.discovery.service;

import app.discovery.domain.AuditLog;
import app.discovery.repo.AuditLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
  private final AuditLogRepository repo;

  public AuditService(AuditLogRepository repo) {
    this.repo = repo;
  }

  @Async
  public void log(Long actorId, String action, String entityType, Long entityId) {
    try {
      repo.save(new AuditLog(actorId, action, entityType, entityId));
    } catch (Exception ignored) {
      // audit must never break the main flow
    }
  }
}
