package com.workout.tracker.service;

import com.workout.tracker.dto.*;

import java.util.List;
import java.util.UUID;

public interface WorkoutPlanExerciseService {
    WorkoutPlanExerciseResponseDTO getWorkoutPlanExerciseById(String username, UUID workoutPlanExerciseId, UUID workoutPlanId);
    WorkoutPlanExerciseResponseDTO createWorkoutPlanExercise(String username, WorkoutPlanExerciseRequestDTO requestDTO, UUID workoutPlanId);
    WorkoutPlanExerciseResponseDTO updateWorkoutPlanExercise(String username, UUID workoutPlanExerciseId, WorkoutPlanExerciseRequestDTO requestDTO, UUID workoutPlanId);
    void deleteWorkoutPlanExercise(String username, UUID workoutPlanExerciseId, UUID workoutPlanId);
    PagedResponse<WorkoutPlanExerciseSummaryDTO> listWorkoutPlanExercises(String username, int page, int size);
    PagedResponse<WorkoutPlanExerciseSummaryDTO> searchWorkoutPlanExercises(String username, UUID workoutPlanId, String query, int page, int size);
}
