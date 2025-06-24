package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutLogsResponseDTO {
    @Schema(description = "Unique Identifier of workout log")
    private UUID id;
    @Schema(description = "Unique Identifier of workout schedule")
    private UUID workoutScheduleId;
    @Schema(description = "Exercise name you have performed")
    private String exerciseName;
    @Schema(description = "Actual Sets you have performed")
    private Integer actualSets;
    @Schema(description = "Actual Reps you have performed")
    private Integer actualReps;
    @Schema(description = "Actual Weights you have lifted")
    private Float actualWeightKg;
    @Schema(description = "You review")
    private String notes;
}
