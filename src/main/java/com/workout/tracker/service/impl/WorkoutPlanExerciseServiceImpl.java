package com.workout.tracker.service.impl;

import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.dto.WorkoutPlanExerciseRequestDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseResponseDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseSummaryDTO;
import com.workout.tracker.exception.CustomAccessDeniedException;
import com.workout.tracker.exception.WorkoutPlanExerciseNotFoundException;
import com.workout.tracker.exception.WorkoutPlanNotFoundException;
import com.workout.tracker.mappers.WorkoutPlanExerciseMapper;
import com.workout.tracker.model.Exercise;
import com.workout.tracker.model.WorkoutPlan;
import com.workout.tracker.model.WorkoutPlanExercise;
import com.workout.tracker.repository.ExerciseRepository;
import com.workout.tracker.repository.WorkoutPlanExerciseRepository;
import com.workout.tracker.repository.WorkoutPlanRepository;
import com.workout.tracker.service.WorkoutPlanExerciseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutPlanExerciseServiceImpl implements WorkoutPlanExerciseService {

    private final WorkoutPlanExerciseRepository workoutPlanExerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanExerciseMapper workoutPlanExerciseMapper;
    private final ExerciseRepository exerciseRepository;

    @Override
    public WorkoutPlanExerciseResponseDTO getWorkoutPlanExerciseById(String username, UUID exerciseId, UUID planId) {
        WorkoutPlanExercise exercise = validateWorkoutPlanExerciseAccess(username, planId, exerciseId);
        return workoutPlanExerciseMapper.toResponseDTO(exercise);
    }

    @Override
    public WorkoutPlanExerciseResponseDTO createWorkoutPlanExercise(String username, WorkoutPlanExerciseRequestDTO requestDTO, UUID workoutPlanId) {
        WorkoutPlan workoutPlan = validateWorkoutPlanAccess(username, workoutPlanId);
        WorkoutPlanExercise wpe = workoutPlanExerciseMapper.toEntity(requestDTO);
        Exercise exercise = exerciseRepository
                .findByName(requestDTO.getExerciseName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Exercise not found: " + requestDTO.getExerciseName()
                ));
        wpe.setExercise(exercise);
        wpe.setWorkoutPlan(workoutPlan);
        WorkoutPlanExercise saved = workoutPlanExerciseRepository.save(wpe);
        return workoutPlanExerciseMapper.toResponseDTO(saved);
    }

    @Override
    public WorkoutPlanExerciseResponseDTO updateWorkoutPlanExercise(String username, UUID exerciseId, WorkoutPlanExerciseRequestDTO requestDTO, UUID workoutPlanId) {
        WorkoutPlanExercise existing = validateWorkoutPlanExerciseAccess(username, workoutPlanId, exerciseId);

        // Fetch real Exercise from DB
        Exercise exercise = exerciseRepository
                .findByName(requestDTO.getExerciseName())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found: " + requestDTO.getExerciseName()));

        // Update manually or with mapper
        WorkoutPlanExercise updated = workoutPlanExerciseMapper.updateFromDTO(existing, requestDTO);
        updated.setExercise(exercise); // 💥 Inject real one here!

        WorkoutPlanExercise saved = workoutPlanExerciseRepository.save(updated);
        return workoutPlanExerciseMapper.toResponseDTO(saved);
    }


    @Override
    public void deleteWorkoutPlanExercise(String username, UUID exerciseId, UUID workoutPlanId) {
        WorkoutPlanExercise exercise = validateWorkoutPlanExerciseAccess(username, workoutPlanId, exerciseId);
        workoutPlanExerciseRepository.deleteById(exerciseId);
    }

    @Override
    public PagedResponse<WorkoutPlanExerciseSummaryDTO> listWorkoutPlanExercises(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WorkoutPlanExercise> workoutPlanExercisePage = workoutPlanExerciseRepository.findByUserUsername(username, pageable);
        Page<WorkoutPlanExerciseSummaryDTO> dtoPage = workoutPlanExercisePage.map(workoutPlanExerciseMapper::toSummaryDTO);
        return pagedResponse(dtoPage);
    }

    @Override
    public PagedResponse<WorkoutPlanExerciseSummaryDTO> searchWorkoutPlanExercises(String username, UUID workoutPlanId, String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WorkoutPlanExercise> workoutPlanExercisePage = workoutPlanExerciseRepository.searchExercises(username, workoutPlanId, query, pageable);
        Page<WorkoutPlanExerciseSummaryDTO> dtoPage = workoutPlanExercisePage.map(workoutPlanExerciseMapper::toSummaryDTO);
        return pagedResponse(dtoPage);
    }

    // ==== Private Helpers Methods ====

    private WorkoutPlan validateWorkoutPlanAccess(String username, UUID workoutPlanId) {
        WorkoutPlan workoutPlan = findWorkoutPlanById(workoutPlanId);
        checkOwnership(username, workoutPlan);
        return workoutPlan;
    }

    private WorkoutPlanExercise validateWorkoutPlanExerciseAccess(String username, UUID workoutPlanId, UUID exerciseId) {
        WorkoutPlan workoutPlan = validateWorkoutPlanAccess(username, workoutPlanId);
        WorkoutPlanExercise workoutPlanExercise = findWorkoutPlanExerciseById(exerciseId);
        if (!workoutPlanExercise.getWorkoutPlan().getId().equals(workoutPlanId)) {
            throw new CustomAccessDeniedException("WorkoutPlanExercise does not belong to the specified WorkoutPlan");
        }
        return workoutPlanExercise;
    }

    private void checkOwnership(String username, WorkoutPlan workoutPlan) {
        if (!workoutPlan.getUser().getUsername().equals(username)) {
            throw new CustomAccessDeniedException("User is not authorized to access this");
        }
    }

    private WorkoutPlanExercise findWorkoutPlanExerciseById(UUID id) {
        return workoutPlanExerciseRepository.findById(id)
                .orElseThrow(() -> new WorkoutPlanExerciseNotFoundException("WorkoutPlanExercise not found"));
    }

    private WorkoutPlan findWorkoutPlanById(UUID id) {
        return workoutPlanRepository.findById(id)
                .orElseThrow(() -> new WorkoutPlanNotFoundException("WorkoutPlan not found"));
    }

    private PagedResponse<WorkoutPlanExerciseSummaryDTO> pagedResponse(Page<WorkoutPlanExerciseSummaryDTO> dto){
        return new PagedResponse<>(
                dto.getContent(),
                dto.getNumber(),
                dto.getSize(),
                dto.getTotalElements(),
                dto.getTotalPages(),
                dto.isLast());
    }
}
