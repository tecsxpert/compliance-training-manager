package com.internship.tool.service;

import com.internship.tool.entity.TrainingRecord;
import com.internship.tool.repository.AuditLogRepository;
import com.internship.tool.repository.TrainingRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TrainingRecordServiceTest {

    @Autowired
    private TrainingRecordService service;

    @Autowired
    private TrainingRecordRepository repository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        auditLogRepository.deleteAll();
    }

    // ✅ TEST CREATE
    @Test
    void testCreate() {
        TrainingRecord record = new TrainingRecord();
        record.setTitle("ISO 27001 Training");
        record.setStatus("PENDING");
        record.setCategory("SECURITY");
        record.setPriority("HIGH");
        record.setAssignedTo("emp1");
        record.setDueDate(LocalDate.now().plusDays(15));

        TrainingRecord saved = service.create(record);
        assertNotNull(saved.getId());
        assertEquals("ISO 27001 Training", saved.getTitle());
        assertEquals("PENDING", saved.getStatus());
    }

    // ✅ TEST UPDATE
    @Test
    void testUpdate() {
        TrainingRecord record = new TrainingRecord();
        record.setTitle("Original Title");
        record.setStatus("PENDING");
        TrainingRecord saved = service.create(record);

        TrainingRecord update = new TrainingRecord();
        update.setTitle("Updated Title");
        update.setStatus("COMPLETED");
        update.setScore(90);

        TrainingRecord result = service.update(saved.getId(), update, "admin");
        assertEquals("Updated Title", result.getTitle());
        assertEquals("COMPLETED", result.getStatus());
        assertEquals(90, result.getScore());

        // Verify audit log was created
        var auditLogs = auditLogRepository.findAll();
        assertFalse(auditLogs.isEmpty());
        assertEquals("UPDATE", auditLogs.get(0).getAction());
    }

    // ✅ TEST DELETE (soft delete)
    @Test
    void testSoftDelete() {
        TrainingRecord record = new TrainingRecord();
        record.setTitle("To Delete");
        record.setStatus("PENDING");
        TrainingRecord saved = service.create(record);

        service.delete(saved.getId(), "admin");

        TrainingRecord result = repository.findById(saved.getId()).orElseThrow();
        assertEquals("DELETED", result.getStatus());

        // Verify audit log
        var logs = auditLogRepository.findAll();
        assertTrue(logs.stream().anyMatch(l -> "DELETE".equals(l.getAction())));
    }

    // ✅ TEST SEARCH
    @Test
    void testSearch() {
        TrainingRecord r1 = new TrainingRecord();
        r1.setTitle("Cybersecurity Training");
        r1.setStatus("PENDING");
        service.create(r1);

        TrainingRecord r2 = new TrainingRecord();
        r2.setTitle("GDPR Training");
        r2.setStatus("COMPLETED");
        service.create(r2);

        List<TrainingRecord> results = service.search("Cyber", null);
        assertEquals(1, results.size());
        assertEquals("Cybersecurity Training", results.get(0).getTitle());

        List<TrainingRecord> byStatus = service.search(null, "COMPLETED");
        assertEquals(1, byStatus.size());
    }

    // ✅ TEST STATS
    @Test
    void testGetStats() {
        for (int i = 0; i < 3; i++) {
            TrainingRecord r = new TrainingRecord();
            r.setTitle("Training " + i);
            r.setStatus("COMPLETED");
            service.create(r);
        }
        TrainingRecord pending = new TrainingRecord();
        pending.setTitle("Pending Training");
        pending.setStatus("PENDING");
        service.create(pending);

        Map<String, Long> stats = service.getStats();
        assertEquals(4L, stats.get("total"));
        assertEquals(3L, stats.get("completed"));
        assertEquals(1L, stats.get("pending"));
    }

    // ✅ TEST UPDATE STATUS
    @Test
    void testUpdateStatus() {
        TrainingRecord record = new TrainingRecord();
        record.setTitle("Status Test");
        record.setStatus("PENDING");
        TrainingRecord saved = service.create(record);

        TrainingRecord updated = service.updateStatus(saved.getId(), "IN_PROGRESS", "manager");
        assertEquals("IN_PROGRESS", updated.getStatus());
    }
}
