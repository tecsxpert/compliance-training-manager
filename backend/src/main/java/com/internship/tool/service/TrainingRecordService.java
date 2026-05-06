package com.internship.tool.service;

import com.internship.tool.entity.AuditLog;
import com.internship.tool.entity.TrainingRecord;
import com.internship.tool.repository.AuditLogRepository;
import com.internship.tool.repository.TrainingRecordRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrainingRecordService {

    private final TrainingRecordRepository repository;
    private final AuditLogRepository auditLogRepository;

    public TrainingRecordService(TrainingRecordRepository repository,
                                  AuditLogRepository auditLogRepository) {
        this.repository = repository;
        this.auditLogRepository = auditLogRepository;
    }

    @Cacheable(value = "training_records", key = "#page + '-' + #size + '-' + #sortBy + '-' + #sortDir")
    public Page<TrainingRecord> getAll(int page, int size, String sortBy, String sortDir) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return repository.findAll(PageRequest.of(page, size, sort));
    }

    @CacheEvict(value = "training_records", allEntries = true)
    public TrainingRecord create(TrainingRecord record) {
        return repository.save(record);
    }

    @CacheEvict(value = "training_records", allEntries = true)
    public TrainingRecord update(Long id, TrainingRecord updated, String performedBy) {
        TrainingRecord record = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + id));

        String oldValue = record.toString();

        record.setTitle(updated.getTitle());
        record.setDescription(updated.getDescription());
        record.setStatus(updated.getStatus());
        record.setPriority(updated.getPriority());
        record.setAssignedTo(updated.getAssignedTo());
        record.setDueDate(updated.getDueDate());
        record.setScore(updated.getScore());
        record.setComplianceScore(updated.getComplianceScore());
        record.setCategory(updated.getCategory());

        TrainingRecord saved = repository.save(record);

        logAudit("TrainingRecord", id, "UPDATE", oldValue, saved.toString(), performedBy);
        return saved;
    }

    @CacheEvict(value = "training_records", allEntries = true)
    public void delete(Long id, String performedBy) {
        TrainingRecord record = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + id));

        String oldValue = record.toString();
        record.setStatus("DELETED");
        repository.save(record);

        logAudit("TrainingRecord", id, "DELETE", oldValue, "DELETED", performedBy);
    }

    @CacheEvict(value = "training_records", allEntries = true)
    public TrainingRecord updateStatus(Long id, String status, String performedBy) {
        TrainingRecord record = repository.findById(id).orElseThrow();
        record.setStatus(status);
        TrainingRecord saved = repository.save(record);
        logAudit("TrainingRecord", id, "STATUS_UPDATE", null, status, performedBy);
        return saved;
    }

    public List<TrainingRecord> search(String q, String status) {
        if (q != null && !q.isBlank()) {
            return repository.findByTitleContainingIgnoreCase(q);
        }
        if (status != null && !status.isBlank()) {
            return repository.findByStatusList(status);
        }
        return repository.findAll();
    }

    @Cacheable(value = "training_stats")
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", repository.count());
        stats.put("completed", repository.countByStatus("COMPLETED"));
        stats.put("pending", repository.countByStatus("PENDING"));
        stats.put("overdue", repository.countByStatus("OVERDUE"));
        stats.put("inProgress", repository.countByStatus("IN_PROGRESS"));
        return stats;
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }

    public List<TrainingRecord> getAll() {
        return repository.findAll();
    }

    private void logAudit(String entityType, Long entityId, String action,
                           String oldValue, String newValue, String performedBy) {
        AuditLog log = new AuditLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setPerformedBy(performedBy);
        log.setPerformedAt(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
