package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanExerciseSummaryDTO {
    @Schema(description = "Unique Identifier for Workout Plan Exercise")
    private UUID id;
    @Schema(description = "Exercise Name")
    private String exerciseName;
    @Schema(description = "Number of Sets to be performed")
    private Integer sets;
    @Schema(description = "Number of Reps per Sets to be performed")
    private Integer reps;
}
