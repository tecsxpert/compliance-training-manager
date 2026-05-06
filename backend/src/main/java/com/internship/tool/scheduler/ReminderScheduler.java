package com.internship.tool.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReminderScheduler {

    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyReminder() {
        System.out.println("Running compliance reminder job...");
    }
}