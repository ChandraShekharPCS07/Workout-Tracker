package com.workout.tracker.service;

import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.dto.WorkoutScheduleRequestDTO;
import com.workout.tracker.dto.WorkoutScheduleResponseDTO;
import com.workout.tracker.dto.WorkoutScheduleSummaryDTO;
import com.workout.tracker.model.Status;

import java.util.List;
import java.util.UUID;

public interface WorkoutScheduleService {
    WorkoutScheduleResponseDTO getWorkoutScheduleById(String username, UUID id);
    WorkoutScheduleResponseDTO createWorkoutSchedule(String username, WorkoutScheduleRequestDTO requestDTO);
    WorkoutScheduleResponseDTO updateWorkoutSchedule(String username, UUID id, WorkoutScheduleRequestDTO requestDTO);
    void deleteWorkoutSchedule(String username, UUID id);
    WorkoutScheduleResponseDTO completeWorkoutSchedule(String username, UUID workoutScheduleId, String notes);

    PagedResponse<WorkoutScheduleSummaryDTO> listWorkoutSchedules(String username, int page, int size);
    PagedResponse<WorkoutScheduleSummaryDTO> listWorkoutSchedulesByStatus(String username, Status status, int page, int size);
    PagedResponse<WorkoutScheduleSummaryDTO> searchWorkoutSchedules(String username, String query, int page, int size);
}
