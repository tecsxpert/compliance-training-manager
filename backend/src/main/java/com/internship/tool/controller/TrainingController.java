package com.internship.tool.controller;

import com.internship.tool.dto.DashboardResponse;
import com.internship.tool.dto.TrainingRequest;
import com.internship.tool.dto.TrainingResponse;
import com.internship.tool.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService service;

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {
        return service.getDashboardMetrics();
    }

    @PostMapping
    public TrainingResponse create(@Valid @RequestBody TrainingRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<TrainingResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TrainingResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public TrainingResponse update(@PathVariable Long id,
                                   @RequestBody TrainingRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}