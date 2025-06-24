package com.workout.tracker.service;

import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.dto.WorkoutLogsRequestDTO;
import com.workout.tracker.dto.WorkoutLogsResponseDTO;

import java.util.UUID;

public interface WorkoutLogsService {
    PagedResponse<WorkoutLogsResponseDTO> getAllWorkoutLogs(String username, int page, int size);
    WorkoutLogsResponseDTO getWorkoutLogById(String username, UUID id);
    WorkoutLogsResponseDTO createWorkoutLog(String username, WorkoutLogsRequestDTO requestDTO);
    WorkoutLogsResponseDTO updateWorkoutLogById(String username, UUID id, WorkoutLogsRequestDTO requestDTO);
    void deleteWorkoutLogById(String username, UUID id);
}
