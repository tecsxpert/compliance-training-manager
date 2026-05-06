package com.internship.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
public class ComplianceTrainingManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComplianceTrainingManagerApplication.class, args);
    }
}
