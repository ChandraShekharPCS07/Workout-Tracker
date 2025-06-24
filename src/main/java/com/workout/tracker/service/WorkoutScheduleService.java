package com.workout.tracker.service;

import com.workout.tracker.dto.WorkoutScheduleRequestDTO;
import com.workout.tracker.dto.WorkoutScheduleResponseDTO;
import com.workout.tracker.dto.WorkoutScheduleSummaryDTO;

import java.util.List;
import java.util.UUID;

public interface WorkoutScheduleService {
    List<WorkoutScheduleSummaryDTO> getAllUpcomingWorkoutSchedulesByUsername(String username);
    List<WorkoutScheduleSummaryDTO> getAllCompletedWorkoutSchedulesByUsername(String username);
    WorkoutScheduleResponseDTO getWorkoutScheduleById(String username, UUID id);
    WorkoutScheduleResponseDTO createWorkoutSchedule(String username, WorkoutScheduleRequestDTO requestDTO);
    WorkoutScheduleResponseDTO updateWorkoutSchedule(String username, UUID id, WorkoutScheduleRequestDTO requestDTO);
    void deleteWorkoutSchedule(String username, UUID id);
    WorkoutScheduleResponseDTO completeWorkoutSchedule(String username, UUID workoutScheduleId, String notes);
}
