package com.workout.tracker.mappers;

import com.workout.tracker.dto.WorkoutPlanExerciseSummaryDTO;
import com.workout.tracker.dto.WorkoutPlanRequestDTO;
import com.workout.tracker.dto.WorkoutPlanResponseDTO;
import com.workout.tracker.dto.WorkoutPlanSummaryDTO;
import com.workout.tracker.model.WorkoutPlan;
import com.workout.tracker.model.WorkoutPlanExercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {WorkoutPlanExerciseMapper.class})
public abstract class WorkoutPlanMapper {

    @Autowired
    protected WorkoutPlanExerciseMapper workoutPlanExerciseMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "exerciseList", ignore = true) // Don't auto-map exercises during creation
    public abstract WorkoutPlan toEntity(WorkoutPlanRequestDTO dto);

    // We’ll override this manually below, so don’t remove this method.
    public abstract WorkoutPlanSummaryDTO toSummary(WorkoutPlan entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "exerciseList", ignore = true)
    public abstract WorkoutPlan updateFromDto(@MappingTarget WorkoutPlan existing, WorkoutPlanRequestDTO dto);

    /**
     * Custom mapping logic to manually map exerciseList using the WorkoutPlanExerciseMapper.
     */
    public WorkoutPlanResponseDTO toResponse(WorkoutPlan entity) {
        if (entity == null) return null;

        WorkoutPlanResponseDTO dto = new WorkoutPlanResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());

        // ✨ This is the key part: we map the list manually using the injected mapper
        if (entity.getExerciseList() != null) {
            List<WorkoutPlanExerciseSummaryDTO> exerciseDTOs = entity.getExerciseList().stream()
                    .map(workoutPlanExerciseMapper::toSummaryDTO)
                    .collect(Collectors.toList());
            dto.setExerciseList(exerciseDTOs);
        }

        return dto;
    }
}
