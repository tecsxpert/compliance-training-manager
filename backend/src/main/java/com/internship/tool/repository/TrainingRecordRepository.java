package com.internship.tool.repository;

import com.internship.tool.entity.TrainingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long> {

    // Pageable variants (for pagination)
    Page<TrainingRecord> findByStatus(String status, Pageable pageable);
    Page<TrainingRecord> findByCategory(String category, Pageable pageable);

    // List variants (for search/scheduler)
    @Query("SELECT t FROM TrainingRecord t WHERE t.status = :status")
    List<TrainingRecord> findByStatusList(@Param("status") String status);

    // Find by due date
    List<TrainingRecord> findByDueDateBefore(LocalDate date);
    List<TrainingRecord> findByDueDateBetween(LocalDate start, LocalDate end);
    List<TrainingRecord> findByDueDateBeforeAndStatusNot(LocalDate date, String status);

    // Full-text search
    @Query("SELECT t FROM TrainingRecord t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(t.assignedTo) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<TrainingRecord> search(@Param("q") String q, Pageable pageable);

    // For title search (used in controller/tests)
    List<TrainingRecord> findByTitleContainingIgnoreCase(String keyword);

    // Upcoming due
    @Query("SELECT t FROM TrainingRecord t WHERE t.dueDate BETWEEN :start AND :end AND t.status <> 'COMPLETED'")
    List<TrainingRecord> findUpcomingDue(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // Aggregate queries
    @Query("SELECT t.status, COUNT(t) FROM TrainingRecord t GROUP BY t.status")
    List<Object[]> countByStatus();

    @Query("SELECT t.category, COUNT(t) FROM TrainingRecord t GROUP BY t.category")
    List<Object[]> countByCategory();

    @Query("SELECT AVG(t.complianceScore) FROM TrainingRecord t WHERE t.complianceScore IS NOT NULL")
    Double avgComplianceScore();

    long countByStatus(String status);
}