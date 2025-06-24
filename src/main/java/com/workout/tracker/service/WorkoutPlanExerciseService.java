package com.workout.tracker.service;

import com.workout.tracker.dto.WorkoutPlanExerciseRequestDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseResponseDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseSummaryDTO;

import java.util.List;
import java.util.UUID;

public interface WorkoutPlanExerciseService {
    List<WorkoutPlanExerciseSummaryDTO> getAllWorkoutPlanExerciseByWorkoutPlanId(String username, UUID workoutPlanId);
    WorkoutPlanExerciseResponseDTO getWorkoutPlanExerciseById(String username, UUID workoutPlanExerciseId, UUID workoutPlanId);
    WorkoutPlanExerciseResponseDTO createWorkoutPlanExercise(String username, WorkoutPlanExerciseRequestDTO requestDTO, UUID workoutPlanId);
    WorkoutPlanExerciseResponseDTO updateWorkoutPlanExercise(String username, UUID workoutPlanExerciseId, WorkoutPlanExerciseRequestDTO requestDTO, UUID workoutPlanId);
    void deleteWorkoutPlanExercise(String username, UUID workoutPlanExerciseId, UUID workoutPlanId);
}
