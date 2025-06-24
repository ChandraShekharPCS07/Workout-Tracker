package com.workout.tracker.mappers;

import com.workout.tracker.dto.WorkoutPlanExerciseRequestDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseResponseDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseSummaryDTO;
import com.workout.tracker.model.Exercise;
import com.workout.tracker.model.WorkoutPlanExercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface WorkoutPlanExerciseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workoutPlan", ignore = true)
    @Mapping(source = "exerciseName", target = "exercise", qualifiedByName = "mapExercise")
    WorkoutPlanExercise toEntity(WorkoutPlanExerciseRequestDTO requestDTO);

    @Mapping(source = "exercise.name", target = "exerciseName")
    WorkoutPlanExerciseResponseDTO toResponseDTO(WorkoutPlanExercise workoutPlanExercise);

    @Mapping(source = "exercise.name", target = "exerciseName")
    WorkoutPlanExerciseSummaryDTO toSummaryDTO(WorkoutPlanExercise workoutPlanExercise);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workoutPlan", ignore = true)
    @Mapping(target = "exercise", ignore = true) // 👈 Add this
    WorkoutPlanExercise updateFromDTO(
            @MappingTarget WorkoutPlanExercise workoutPlanExercise,
            WorkoutPlanExerciseRequestDTO requestDTO);


    @Named("mapExercise")
    default Exercise mapExercise(String name){
        Exercise exercise = new Exercise();
        exercise.setName(name);
        return exercise;
    }
}
