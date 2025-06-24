package com.workout.tracker.service.impl;

import com.workout.tracker.dto.ExerciseRequestDTO;
import com.workout.tracker.dto.ExerciseResponseDTO;
import com.workout.tracker.dto.ExerciseSummaryDTO;
import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.exception.ExerciseNotFoundException;
import com.workout.tracker.mappers.ExerciseMapper;
import com.workout.tracker.model.Exercise;
import com.workout.tracker.repository.ExerciseRepository;
import com.workout.tracker.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Override
    public PagedResponse<ExerciseSummaryDTO> getAllExercises(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("category").ascending());
        Page<Exercise> exercisePage = exerciseRepository.findAll(pageable);
        Page<ExerciseSummaryDTO> dtosPage = exercisePage.map(exerciseMapper::toSummary);
        return pagedResponse(dtosPage);
    }

    @Override
    public PagedResponse<ExerciseSummaryDTO> getAllExercisesByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Exercise> exercisePage = exerciseRepository.findByCategory(category, pageable);
        Page<ExerciseSummaryDTO> dtosPage = exercisePage.map(exerciseMapper::toSummary);
        return pagedResponse(dtosPage);
    }

    @Override
    public PagedResponse<ExerciseSummaryDTO> getAllExercisesByMuscleGroup(String muscleGroup, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Exercise> exercisePage = exerciseRepository.findByMuscleGroup(muscleGroup, pageable);
        Page<ExerciseSummaryDTO> dtosPage = exercisePage.map(exerciseMapper::toSummary);
        return pagedResponse(dtosPage);
    }

    @Override
    public ExerciseResponseDTO getExerciseById(UUID id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        return exerciseMapper.toResponse(exercise);
    }

    @Override
    public ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseRequestDTO) {
        Exercise exercise = exerciseMapper.toEntity(exerciseRequestDTO);
        Exercise savedExercise = exerciseRepository.save(exercise);
        return exerciseMapper.toResponse(savedExercise);
    }

    @Override
    public ExerciseResponseDTO updateExercise(UUID id, ExerciseRequestDTO exerciseRequestDTO) {
        Exercise existing = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found"));
        Exercise updatedExercise = exerciseMapper.UpdateFromDto(existing, exerciseRequestDTO);
        return exerciseMapper.toResponse(exerciseRepository.save(updatedExercise));
    }

    @Override
    public void deleteExercise(UUID id) {
        if (!exerciseRepository.existsById(id)) {
            throw new ExerciseNotFoundException("Exercise not found");
        }
        exerciseRepository.deleteById(id);
    }

    private PagedResponse<ExerciseSummaryDTO> pagedResponse(Page<ExerciseSummaryDTO> dto){
        return new PagedResponse<>(
                dto.getContent(),
                dto.getNumber(),
                dto.getSize(),
                dto.getTotalElements(),
                dto.getTotalPages(),
                dto.isLast());
    }
}
