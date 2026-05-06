package com.internship.tool.repository;

import com.internship.tool.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    long countByStatus(String status);
}