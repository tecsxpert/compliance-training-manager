package com.internship.tool.repository;

import org.springframework.data.repository.query.Param;
import com.internship.tool.entity.TrainingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;



@Repository
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long> {

    // Filter by status
    List<TrainingRecord> findByStatus(String status);

    // Date range
    List<TrainingRecord> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    // Search
    List<TrainingRecord> findByTitleContainingIgnoreCase(String keyword);

    // Custom @Query
    @Query("SELECT t FROM TrainingRecord t WHERE t.status = :status AND t.dueDate >= :date")
    List<TrainingRecord> findActiveTasks(@Param("status") String status, @Param("date") LocalDate date);
    List<TrainingRecord> findByDueDateBefore(LocalDate date);
}