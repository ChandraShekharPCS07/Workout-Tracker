package com.workout.tracker.controller;

import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.dto.WorkoutLogsRequestDTO;
import com.workout.tracker.dto.WorkoutLogsResponseDTO;
import com.workout.tracker.service.WorkoutLogsService;
import com.workout.tracker.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workout-logs")
@RequiredArgsConstructor
public class WorkoutLogsController {

    private final WorkoutLogsService workoutLogsService;

    @GetMapping
    @Operation(summary = "Get all workout logs for a user")
    public ResponseEntity<PagedResponse<WorkoutLogsResponseDTO>> getAllWorkoutLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String username = SecurityUtils.getCurrentUsername();
        PagedResponse<WorkoutLogsResponseDTO> response = workoutLogsService.getAllWorkoutLogs(username, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single workout log by ID")
    public ResponseEntity<WorkoutLogsResponseDTO> getWorkoutLogById(
            @PathVariable UUID id
    ) {
        String username = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(workoutLogsService.getWorkoutLogById(username, id));
    }

    @PostMapping
    @Operation(summary = "Create a new workout log")
    public ResponseEntity<WorkoutLogsResponseDTO> createWorkoutLog(
            @Valid @RequestBody WorkoutLogsRequestDTO requestDTO
    ) {
        String username = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(workoutLogsService.createWorkoutLog(username, requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing workout log")
    public ResponseEntity<WorkoutLogsResponseDTO> updateWorkoutLogById(
            @PathVariable UUID id,
            @Valid @RequestBody WorkoutLogsRequestDTO requestDTO
    ) {
        String username = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(workoutLogsService.updateWorkoutLogById(username, id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a workout log")
    public ResponseEntity<Void> deleteWorkoutLogById(
            @PathVariable UUID id
    ) {
        String username = SecurityUtils.getCurrentUsername();
        workoutLogsService.deleteWorkoutLogById(username, id);
        return ResponseEntity.noContent().build();
    }
}

