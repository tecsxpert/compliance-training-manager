package com.internship.tool.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "training_records")
@EntityListeners(AuditingEntityListener.class)
public class TrainingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "status")
    private String status = "PENDING";

    @Column(name = "compliance_score", precision = 5, scale = 2)
    private BigDecimal complianceScore;

    @Column(name = "priority")
    private String priority;

    @Column(name = "score")
    private Integer score;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Column(name = "ai_description", columnDefinition = "TEXT")
    private String aiDescription;

    @Column(name = "ai_recommendations", columnDefinition = "TEXT")
    private String aiRecommendations;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    public TrainingRecord() {}

    public TrainingRecord(Long id, String title, String description, String category, String status, BigDecimal complianceScore, String priority, Integer score, String assignedTo, LocalDate dueDate, LocalDate completedDate, String aiDescription, String aiRecommendations, String filePath, String fileName, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.complianceScore = complianceScore;
        this.priority = priority;
        this.score = score;
        this.assignedTo = assignedTo;
        this.dueDate = dueDate;
        this.completedDate = completedDate;
        this.aiDescription = aiDescription;
        this.aiRecommendations = aiRecommendations;
        this.filePath = filePath;
        this.fileName = fileName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return this.category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getComplianceScore() { return this.complianceScore; }
    public void setComplianceScore(BigDecimal complianceScore) { this.complianceScore = complianceScore; }

    public String getPriority() { return this.priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Integer getScore() { return this.score; }
    public void setScore(Integer score) { this.score = score; }

    public String getAssignedTo() { return this.assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public LocalDate getDueDate() { return this.dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getCompletedDate() { return this.completedDate; }
    public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }

    public String getAiDescription() { return this.aiDescription; }
    public void setAiDescription(String aiDescription) { this.aiDescription = aiDescription; }

    public String getAiRecommendations() { return this.aiRecommendations; }
    public void setAiRecommendations(String aiRecommendations) { this.aiRecommendations = aiRecommendations; }

    public String getFilePath() { return this.filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileName() { return this.fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return this.createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return this.updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

}