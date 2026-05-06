package com.internship.tool.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "training_record")
public class TrainingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;
    private String priority;
    private String assignedTo;
    private LocalDate dueDate;
    private Integer score;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}