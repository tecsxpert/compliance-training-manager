package com.internship.tool.controller;

import com.internship.tool.repository.TrainingRecordRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard analytics and summary endpoints")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final TrainingRecordRepository repository;

    public DashboardController(TrainingRecordRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Get dashboard summary statistics")
    @GetMapping("/summary")
    @Cacheable(value = "training_stats", key = "'dashboard-summary'")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();

        // Overall counts
        summary.put("totalRecords", repository.count());
        summary.put("completedCount", repository.countByStatus("COMPLETED"));
        summary.put("pendingCount", repository.countByStatus("PENDING"));
        summary.put("inProgressCount", repository.countByStatus("IN_PROGRESS"));
        summary.put("overdueCount", repository.countByStatus("OVERDUE"));

        // Compliance score
        Double avgScore = repository.avgComplianceScore();
        summary.put("averageComplianceScore", avgScore != null ? Math.round(avgScore * 100.0) / 100.0 : 0.0);

        // Completion rate
        long total = repository.count();
        long completed = repository.countByStatus("COMPLETED");
        double completionRate = total > 0 ? (completed * 100.0 / total) : 0.0;
        summary.put("completionRate", Math.round(completionRate * 10.0) / 10.0);

        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "Get status breakdown chart data")
    @GetMapping("/status-breakdown")
    @Cacheable(value = "training_stats", key = "'status-breakdown'")
    public ResponseEntity<List<Map<String, Object>>> getStatusBreakdown() {
        List<Object[]> raw = repository.countByStatus();
        List<Map<String, Object>> result = raw.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("status", row[0]);
                    item.put("count", row[1]);
                    return item;
                })
                .toList();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get category breakdown chart data")
    @GetMapping("/category-breakdown")
    @Cacheable(value = "training_stats", key = "'category-breakdown'")
    public ResponseEntity<List<Map<String, Object>>> getCategoryBreakdown() {
        List<Object[]> raw = repository.countByCategory();
        List<Map<String, Object>> result = raw.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("category", row[0]);
                    item.put("count", row[1]);
                    return item;
                })
                .toList();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get admin-only detailed analytics (ADMIN only)")
    @GetMapping("/analytics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();

        analytics.put("statusBreakdown", repository.countByStatus()
                .stream().map(r -> Map.of("status", r[0], "count", r[1])).toList());
        analytics.put("categoryBreakdown", repository.countByCategory()
                .stream().map(r -> Map.of("category", r[0], "count", r[1])).toList());
        analytics.put("avgComplianceScore", repository.avgComplianceScore());
        analytics.put("totalRecords", repository.count());

        return ResponseEntity.ok(analytics);
    }
}
