package com.workout.tracker.service;


import com.workout.tracker.dto.ExerciseRequestDTO;
import com.workout.tracker.dto.ExerciseResponseDTO;
import com.workout.tracker.dto.ExerciseSummaryDTO;
import com.workout.tracker.dto.PagedResponse;

import java.util.UUID;

public interface ExerciseService {

    /**
     * Returns a paginated list of all exercises.
     * @param page zero-based page index
     * @param size page size
     * @return paged response of ExerciseSummaryDTO
     */
    PagedResponse<ExerciseSummaryDTO> getAllExercises(int page, int size);

    /**
     * Returns a paginated list of exercises filtered by category.
     * @param category category name
     * @param page zero-based page index
     * @param size page size
     * @return paged response of ExerciseSummaryDTO
     */
    PagedResponse<ExerciseSummaryDTO> getExercisesByCategory(String category, int page, int size);

    /**
     * Returns a paginated list of exercises filtered by muscle group.
     * @param muscleGroup muscle group name
     * @param page zero-based page index
     * @param size page size
     * @return paged response of ExerciseSummaryDTO
     */
    PagedResponse<ExerciseSummaryDTO> getExercisesByMuscleGroup(String muscleGroup, int page, int size);

    /**
     * Retrieves full exercise details by ID.
     * @param id UUID of the exercise
     * @return ExerciseResponseDTO with full details
     */
    ExerciseResponseDTO getExerciseById(UUID id);

    /**
     * Creates a new exercise.
     * @param exerciseRequestDTO validated request data
     * @return created ExerciseResponseDTO
     */
    ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseRequestDTO);

    /**
     * Updates an existing exercise.
     * @param id UUID of the exercise to update
     * @param exerciseRequestDTO validated request data
     * @return updated ExerciseResponseDTO
     */
    ExerciseResponseDTO updateExercise(UUID id, ExerciseRequestDTO exerciseRequestDTO);

    /**
     * Deletes an exercise by ID.
     * @param id UUID of the exercise to delete
     */
    void deleteExercise(UUID id);
}
