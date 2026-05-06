package com.internship.tool.controller;

import com.internship.tool.entity.TrainingRecord;
import com.internship.tool.repository.TrainingRecordRepository;
import com.internship.tool.repository.AuditLogRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

// ✅ Using @DataJpaTest instead of @SpringBootTest to avoid loading Redis/Mail/etc.
// This only loads JPA/H2 context — perfect for CRUD integration tests.
@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class TrainingRecordIntegrationTest {

    @Autowired
    private TrainingRecordRepository repository;

    @Autowired
    private AuditLogRepository auditRepository;

    @Test
    void testFullCrudFlow() {

        // CREATE
        TrainingRecord record = new TrainingRecord();
        record.setTitle("Test");
        record.setStatus("PENDING");

        TrainingRecord saved = repository.save(record);
        assertNotNull(saved);
        assertNotNull(saved.getId());

        // READ
        TrainingRecord fetched = repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Record not found"));
        assertNotNull(fetched);

        // UPDATE
        fetched.setStatus("COMPLETED");
        repository.save(fetched);

        TrainingRecord updated = repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Update failed"));

        assertEquals("COMPLETED", updated.getStatus());

        // DELETE (soft delete)
        updated.setStatus("DELETED");
        repository.save(updated);

        TrainingRecord deleted = repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Delete failed"));

        assertEquals("DELETED", deleted.getStatus());
    }
}