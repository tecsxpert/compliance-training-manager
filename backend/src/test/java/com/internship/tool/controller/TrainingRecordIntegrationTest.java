package com.internship.tool.controller;

import com.internship.tool.entity.TrainingRecord;
import com.internship.tool.repository.TrainingRecordRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TrainingRecordIntegrationTest {

    @Autowired
    private TrainingRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    // ✅ TEST FULL CRUD FLOW
    @Test
    void testFullCrudFlow() {
        // CREATE
        TrainingRecord record = new TrainingRecord();
        record.setTitle("GDPR Compliance Training");
        record.setStatus("PENDING");
        record.setCategory("GDPR");
        record.setPriority("HIGH");
        record.setAssignedTo("john.doe");
        record.setDueDate(LocalDate.now().plusDays(30));

        TrainingRecord saved = repository.save(record);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("GDPR Compliance Training", saved.getTitle());

        // READ
        TrainingRecord fetched = repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Record not found"));
        assertEquals("PENDING", fetched.getStatus());
        assertEquals("HIGH", fetched.getPriority());

        // UPDATE
        fetched.setStatus("COMPLETED");
        fetched.setScore(95);
        repository.save(fetched);

        TrainingRecord updated = repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Update failed"));
        assertEquals("COMPLETED", updated.getStatus());
        assertEquals(95, updated.getScore());

        // SEARCH by title
        var searchResults = repository.findByTitleContainingIgnoreCase("GDPR");
        assertFalse(searchResults.isEmpty());
        assertEquals("GDPR Compliance Training", searchResults.get(0).getTitle());

        // COUNT by status
        assertEquals(1, repository.countByStatus("COMPLETED"));
        assertEquals(0, repository.countByStatus("PENDING"));

        // SOFT DELETE
        updated.setStatus("DELETED");
        repository.save(updated);
        TrainingRecord deleted = repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Delete failed"));
        assertEquals("DELETED", deleted.getStatus());
    }

    // ✅ TEST SEARCH QUERY
    @Test
    void testSearchQuery() {
        TrainingRecord r1 = new TrainingRecord();
        r1.setTitle("Security Awareness Training");
        r1.setStatus("PENDING");
        r1.setAssignedTo("alice");
        repository.save(r1);

        TrainingRecord r2 = new TrainingRecord();
        r2.setTitle("GDPR Data Protection");
        r2.setStatus("COMPLETED");
        r2.setAssignedTo("bob");
        repository.save(r2);

        var secResults = repository.findByTitleContainingIgnoreCase("security");
        assertEquals(1, secResults.size());

        var statusResults = repository.findByStatusList("COMPLETED");
        assertEquals(1, statusResults.size());
    }

    // ✅ TEST DUE DATE QUERIES
    @Test
    void testDueDateQueries() {
        TrainingRecord r = new TrainingRecord();
        r.setTitle("Overdue Training");
        r.setStatus("PENDING");
        r.setDueDate(LocalDate.now().minusDays(5));
        repository.save(r);

        var overdue = repository.findByDueDateBeforeAndStatusNot(LocalDate.now(), "COMPLETED");
        assertFalse(overdue.isEmpty());

        var upcoming = repository.findUpcomingDue(LocalDate.now(), LocalDate.now().plusDays(7));
        assertTrue(upcoming.isEmpty()); // The record is overdue, not upcoming
    }

    // ✅ TEST PAGINATION
    @Test
    void testPagination() {
        for (int i = 1; i <= 10; i++) {
            TrainingRecord r = new TrainingRecord();
            r.setTitle("Training " + i);
            r.setStatus("PENDING");
            repository.save(r);
        }

        long total = repository.count();
        assertEquals(10, total);

        var page = repository.findAll(
                org.springframework.data.domain.PageRequest.of(0, 5));
        assertEquals(5, page.getContent().size());
        assertEquals(10, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }
}