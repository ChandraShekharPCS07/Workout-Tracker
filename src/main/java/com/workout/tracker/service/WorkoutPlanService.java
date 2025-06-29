package com.workout.tracker.service;

import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.dto.WorkoutPlanRequestDTO;
import com.workout.tracker.dto.WorkoutPlanResponseDTO;
import com.workout.tracker.dto.WorkoutPlanSummaryDTO;

import java.util.List;
import java.util.UUID;

public interface WorkoutPlanService {
    WorkoutPlanResponseDTO getWorkoutPlanById(String username, UUID workoutPlanId);
    WorkoutPlanResponseDTO createWorkoutPlan( String username, WorkoutPlanRequestDTO requestDTO);
    WorkoutPlanResponseDTO updateWorkoutPlan(String username, UUID workoutPlanId, WorkoutPlanRequestDTO requestDTO);
    void deleteWorkoutPlan(String username, UUID workoutPlanId);
    PagedResponse<WorkoutPlanSummaryDTO> listUserWorkoutPlans(String username, int page, int size);
    PagedResponse<WorkoutPlanSummaryDTO> searchUserWorkoutPlans(String username, String query, int page, int size);
}
