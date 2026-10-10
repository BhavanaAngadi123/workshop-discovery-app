package app.discovery.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_log")
public class AuditLog {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "actor_id")
  private Long actorId;

  @Column(nullable = false, length = 80)
  private String action;

  @Column(name = "entity_type", length = 80)
  private String entityType;

  @Column(name = "entity_id")
  private Long entityId;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  public AuditLog() {}
  public AuditLog(Long actorId, String action, String entityType, Long entityId) {
    this.actorId = actorId; this.action = action; this.entityType = entityType; this.entityId = entityId;
  }

  public Long getId() { return id; }
  public Long getActorId() { return actorId; }
  public String getAction() { return action; }
  public String getEntityType() { return entityType; }
  public Long getEntityId() { return entityId; }
  public Instant getCreatedAt() { return createdAt; }
}
