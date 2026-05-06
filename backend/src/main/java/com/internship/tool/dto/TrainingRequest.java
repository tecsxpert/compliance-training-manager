package com.internship.tool.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingRequest {

    private String title;
    private String description;
    private String status;
    private Integer score;

    // ✅ THIS FIXES YOUR ERROR
    private LocalDateTime dueDate;
}