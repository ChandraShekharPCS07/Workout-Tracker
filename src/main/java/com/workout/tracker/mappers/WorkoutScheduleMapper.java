package com.workout.tracker.mappers;

import com.workout.tracker.dto.WorkoutScheduleRequestDTO;
import com.workout.tracker.dto.WorkoutScheduleResponseDTO;
import com.workout.tracker.dto.WorkoutScheduleSummaryDTO;
import com.workout.tracker.model.WorkoutPlan;
import com.workout.tracker.model.WorkoutSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface WorkoutScheduleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "durationMins", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(source = "workoutPlanName", target = "workoutPlan", qualifiedByName = "mapWorkoutPlan")
    WorkoutSchedule toEntity(WorkoutScheduleRequestDTO requestDTO);

    @Mapping(source = "workoutPlan.name", target = "workoutPlanName")
    WorkoutScheduleResponseDTO toResponseDTO(WorkoutSchedule workoutSchedule);

    @Mapping(source = "workoutPlan.name", target = "workoutPlanName")
    WorkoutScheduleSummaryDTO toSummaryDTO(WorkoutSchedule workoutSchedule);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "durationMins", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(source = "workoutPlanName", target = "workoutPlan", qualifiedByName = "mapWorkoutPlan")
    WorkoutSchedule updateFromDTO(@MappingTarget WorkoutSchedule workoutSchedule, WorkoutScheduleRequestDTO requestDTO);

    @Named("mapWorkoutPlan")
    default WorkoutPlan mapWorkoutPlan(String name){
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setName(name);
        return workoutPlan;
    }

}
