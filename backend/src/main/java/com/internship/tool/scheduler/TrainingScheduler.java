package com.internship.tool.scheduler;

import com.internship.tool.entity.TrainingRecord;
import com.internship.tool.repository.TrainingRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TrainingScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TrainingScheduler.class);

    private final TrainingRecordRepository repository;

    public TrainingScheduler(TrainingRecordRepository repository) {
        this.repository = repository;
    }

    // ✅ DAILY OVERDUE CHECK (9 AM)
    @Scheduled(cron = "0 0 9 * * ?")
    public void checkOverdueTasks() {
        LocalDate today = LocalDate.now();
        List<TrainingRecord> overdue = repository.findByDueDateBeforeAndStatusNot(today, "COMPLETED");
        for (TrainingRecord r : overdue) {
            logger.warn("OVERDUE TRAINING: [{}] - {} (due: {})", r.getId(), r.getTitle(), r.getDueDate());
        }
        logger.info("Overdue check complete. {} overdue records found.", overdue.size());
    }

    // ✅ 7-DAY UPCOMING ALERT (10 AM)
    @Scheduled(cron = "0 0 10 * * ?")
    public void upcomingTasks() {
        LocalDate today = LocalDate.now();
        LocalDate next7 = today.plusDays(7);
        List<TrainingRecord> upcoming = repository.findUpcomingDue(today, next7);
        for (TrainingRecord r : upcoming) {
            logger.info("UPCOMING TRAINING: [{}] - {} (due: {})", r.getId(), r.getTitle(), r.getDueDate());
        }
        logger.info("Upcoming check complete. {} records due in next 7 days.", upcoming.size());
    }

    // ✅ WEEKLY SUMMARY (Monday 8 AM)
    @Scheduled(cron = "0 0 8 ? * MON")
    public void weeklySummary() {
        long total = repository.count();
        long completed = repository.countByStatus("COMPLETED");
        long pending = repository.countByStatus("PENDING");
        long overdue = repository.countByStatus("OVERDUE");
        logger.info("=== WEEKLY COMPLIANCE SUMMARY ===");
        logger.info("Total: {} | Completed: {} | Pending: {} | Overdue: {}", total, completed, pending, overdue);
    }
}