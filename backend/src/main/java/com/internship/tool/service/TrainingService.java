package com.internship.tool.service;

import com.internship.tool.dto.DashboardResponse;
import com.internship.tool.dto.TrainingRequest;
import com.internship.tool.dto.TrainingResponse;
import com.internship.tool.entity.Training;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository repository;

    // CREATE
    @CacheEvict(value = { "trainingsAll", "training" }, allEntries = true)
    public TrainingResponse create(TrainingRequest request) {

        Objects.requireNonNull(request, "Request cannot be null");

        Training training = Training.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .dueDate(request.getDueDate())
                .build();

        Training saved = repository.save(training);

        return mapToResponse(saved);
    }

    // GET ALL
    @Cacheable(value = "trainingsAll")
    public List<TrainingResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // DASHBOARD METRICS
    public DashboardResponse getDashboardMetrics() {
        long total = repository.count();
        long pending = repository.countByStatus("PENDING");
        long completed = repository.countByStatus("COMPLETED");

        return DashboardResponse.builder()
                .totalTrainings(total)
                .pendingTrainings(pending)
                .completedTrainings(completed)
                .build();
    }

    // GET BY ID
    @Cacheable(value = "training", key = "#id")
    public TrainingResponse getById(Long id) {

        Objects.requireNonNull(id, "ID cannot be null");

        Training training = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training not found"));

        return mapToResponse(training);
    }

    // UPDATE
    @CacheEvict(value = { "trainingsAll", "training" }, allEntries = true)
    public TrainingResponse update(Long id, TrainingRequest request) {

        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(request, "Request cannot be null");

        Training training = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training not found"));

        training.setTitle(request.getTitle());
        training.setDescription(request.getDescription());
        training.setStatus(request.getStatus());
        training.setDueDate(request.getDueDate());

        return mapToResponse(repository.save(training));
    }

    // DELETE
    @CacheEvict(value = { "trainingsAll", "training" }, allEntries = true)
    public void delete(Long id) {

        Objects.requireNonNull(id, "ID cannot be null");

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Training not found");
        }

        repository.deleteById(id);
    }

    // MAPPER
    private TrainingResponse mapToResponse(Training t) {

        Objects.requireNonNull(t, "Training cannot be null");

        return TrainingResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .status(t.getStatus())
                .dueDate(t.getDueDate())
                .createdAt(t.getCreatedAt())
                .build();
    }
}