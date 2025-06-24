package com.workout.tracker.mappers;

import com.workout.tracker.dto.WorkoutLogsRequestDTO;
import com.workout.tracker.dto.WorkoutLogsResponseDTO;
import com.workout.tracker.model.Exercise;
import com.workout.tracker.model.WorkoutLogs;
import com.workout.tracker.model.WorkoutSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface WorkoutLogsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "workoutScheduleId", target = "workoutSchedule", qualifiedByName = "mapWorkoutSchedule")
    @Mapping(source = "exerciseName", target = "exercise", qualifiedByName = "mapExercise")
    WorkoutLogs toEntity(WorkoutLogsRequestDTO requestDTO);

    @Mapping(source = "workoutSchedule.id", target = "workoutScheduleId")
    @Mapping(source = "exercise.name", target = "exerciseName")
    WorkoutLogsResponseDTO toResponseDTO(WorkoutLogs workoutLogs);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "workoutScheduleId", target = "workoutSchedule", qualifiedByName = "mapWorkoutSchedule")
    @Mapping(source = "exerciseName", target = "exercise", qualifiedByName = "mapExercise")
    WorkoutLogs updateFromDTO(@MappingTarget WorkoutLogs workoutLogs, WorkoutLogsRequestDTO requestDTO);

    @Named("mapWorkoutSchedule")
    default WorkoutSchedule mapWorkoutSchedule(UUID id){
        WorkoutSchedule workoutSchedule = new WorkoutSchedule();
        workoutSchedule.setId(id);
        return workoutSchedule;
    }

    @Named("mapExercise")
    default Exercise mapExercise(String name){
        return Optional.ofNullable(name)
                .map(n -> {
                    Exercise ex = new Exercise();
                    ex.setName(n);
                    return ex;
                }).orElse(null);
    }
}
