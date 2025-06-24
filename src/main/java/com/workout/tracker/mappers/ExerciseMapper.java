package com.workout.tracker.mappers;

import com.workout.tracker.dto.ExerciseRequestDTO;
import com.workout.tracker.dto.ExerciseResponseDTO;
import com.workout.tracker.dto.ExerciseSummaryDTO;
import com.workout.tracker.model.Exercise;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ExerciseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Exercise toEntity(ExerciseRequestDTO dto);

    ExerciseResponseDTO toResponse(Exercise entity);

    ExerciseSummaryDTO toSummary(Exercise entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Exercise UpdateFromDto(@MappingTarget Exercise existing, ExerciseRequestDTO dto);
}

