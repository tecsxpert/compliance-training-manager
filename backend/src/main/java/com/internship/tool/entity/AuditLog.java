package com.internship.tool.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "performed_at")
    private LocalDateTime performedAt = LocalDateTime.now();

    public AuditLog() {}

    public AuditLog(Long id, String entityType, Long entityId, String action, String oldValue, String newValue, String performedBy, LocalDateTime performedAt) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.performedBy = performedBy;
        this.performedAt = performedAt;
    }

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public String getEntityType() { return this.entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public Long getEntityId() { return this.entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public String getAction() { return this.action; }
    public void setAction(String action) { this.action = action; }

    public String getOldValue() { return this.oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }

    public String getNewValue() { return this.newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }

    public String getPerformedBy() { return this.performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public LocalDateTime getPerformedAt() { return this.performedAt; }
    public void setPerformedAt(LocalDateTime performedAt) { this.performedAt = performedAt; }

}