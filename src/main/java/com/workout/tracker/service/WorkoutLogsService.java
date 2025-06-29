package com.workout.tracker.service;

import com.workout.tracker.dto.*;

import java.util.UUID;

public interface WorkoutLogsService {
    WorkoutLogsResponseDTO getWorkoutLogById(String username, UUID id);
    WorkoutLogsResponseDTO createWorkoutLog(String username, WorkoutLogsRequestDTO requestDTO);
    WorkoutLogsResponseDTO updateWorkoutLogById(String username, UUID id, WorkoutLogsRequestDTO requestDTO);
    void deleteWorkoutLogById(String username, UUID id);

//    PagedResponse<WorkoutLogsSummaryDTO> listWorkoutLogs(String username, int page, int size);
//    PagedResponse<WorkoutLogsSummaryDTO> listWorkoutLogsBySchedule(String username, UUID workoutScheduleId, int page, int size);
//    PagedResponse<WorkoutLogsSummaryDTO> listWorkoutLogsByExercise(String username, UUID exerciseId, int page, int size);
//    PagedResponse<WorkoutLogsSummaryDTO> searchWorkoutLogs(String username, String query, int page, int size);
//    PagedResponse<WorkoutLogsSummaryDTO> filterWorkoutLogs(String username, WorkoutLogsFilterRequestDTO filter, int page, int size);
}
