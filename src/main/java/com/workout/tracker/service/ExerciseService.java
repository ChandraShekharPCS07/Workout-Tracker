package com.workout.tracker.service;


import com.workout.tracker.dto.ExerciseRequestDTO;
import com.workout.tracker.dto.ExerciseResponseDTO;
import com.workout.tracker.dto.ExerciseSummaryDTO;
import com.workout.tracker.dto.PagedResponse;

import java.util.UUID;

public interface ExerciseService {
    PagedResponse<ExerciseSummaryDTO> getAllExercises(int page, int size);
    PagedResponse<ExerciseSummaryDTO> getAllExercisesByCategory(String category, int page, int size);
    PagedResponse<ExerciseSummaryDTO> getAllExercisesByMuscleGroup(String muscleGroup, int page, int size);
    ExerciseResponseDTO getExerciseById(UUID id);
    ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseRequestDTO);
    ExerciseResponseDTO updateExercise(UUID id, ExerciseRequestDTO exerciseRequestDTO);
    void deleteExercise(UUID id);
}
