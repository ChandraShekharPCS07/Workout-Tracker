package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanResponseDTO {
    @Schema(description = "Unique Identifier for WorkoutPlan")
    private UUID id;
    @Schema(description = "Name of the workout plan")
    private String name;
    @Schema(description = "Some info about how to perform exercises")
    private String notes;
    @Schema(description = "This date represents when was this plan created")
    private LocalDateTime createdAt;
    @Schema(description = "Exercise List")
    private List<WorkoutPlanExerciseSummaryDTO> exerciseList;
}

