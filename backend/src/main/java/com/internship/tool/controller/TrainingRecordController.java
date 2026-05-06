package com.internship.tool.controller;

import com.internship.tool.entity.AuditLog;
import com.internship.tool.entity.TrainingRecord;
import com.internship.tool.service.TrainingRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/training")
@Tag(name = "Training Records", description = "Manage compliance training records")
@SecurityRequirement(name = "bearerAuth")
public class TrainingRecordController {

    private final TrainingRecordService service;

    public TrainingRecordController(TrainingRecordService service) {
        this.service = service;
    }

    // ✅ GET ALL WITH PAGINATION
    @Operation(summary = "Get all training records with pagination")
    @GetMapping
    public ResponseEntity<Page<TrainingRecord>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(service.getAll(page, size, sortBy, sortDir));
    }

    // ✅ CREATE
    @Operation(summary = "Create a new training record")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TrainingRecord> create(
            @RequestBody TrainingRecord record,
            Authentication auth) {
        record.setCreatedBy(auth != null ? auth.getName() : "system");
        return ResponseEntity.ok(service.create(record));
    }

    // ✅ UPDATE
    @Operation(summary = "Update a training record (ADMIN or MANAGER only)")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TrainingRecord> update(
            @PathVariable Long id,
            @RequestBody TrainingRecord updated,
            Authentication auth) {
        String performedBy = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.update(id, updated, performedBy));
    }

    // ✅ DELETE (soft delete)
    @Operation(summary = "Soft-delete a training record (ADMIN only)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable Long id,
            Authentication auth) {
        String performedBy = auth != null ? auth.getName() : "system";
        service.delete(id, performedBy);
        return ResponseEntity.ok(Map.of("message", "Record deleted successfully"));
    }

    // ✅ SEARCH
    @Operation(summary = "Search training records by title or status")
    @GetMapping("/search")
    public ResponseEntity<List<TrainingRecord>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.search(q, status));
    }

    // ✅ STATS
    @Operation(summary = "Get training statistics")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> stats() {
        return ResponseEntity.ok(service.getStats());
    }

    // ✅ UPDATE STATUS
    @Operation(summary = "Update the status of a training record")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TrainingRecord> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication auth) {
        String performedBy = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.updateStatus(id, status, performedBy));
    }

    // ✅ GET AUDIT LOGS
    @Operation(summary = "Get all audit logs (ADMIN only)")
    @GetMapping("/audit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(service.getAuditLogs());
    }

    // ✅ CSV EXPORT
    @Operation(summary = "Export all training records as CSV")
    @GetMapping(value = "/export", produces = "text/plain")
    public ResponseEntity<byte[]> exportCSV() {
        List<TrainingRecord> records = service.getAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.println("ID,Title,Status,Priority,Score,DueDate,AssignedTo,Category");
        for (TrainingRecord r : records) {
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                    r.getId(),
                    nvl(r.getTitle()),
                    nvl(r.getStatus()),
                    nvl(r.getPriority()),
                    r.getScore() != null ? r.getScore() : "",
                    r.getDueDate() != null ? r.getDueDate() : "",
                    nvl(r.getAssignedTo()),
                    nvl(r.getCategory())
            );
        }
        writer.flush();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=training_report.csv")
                .header("Content-Type", "text/csv")
                .body(out.toByteArray());
    }

    private String nvl(String s) {
        return s != null ? s : "";
    }
}