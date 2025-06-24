package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanExerciseResponseDTO {
    @Schema(description = "Unique Identifier for Workout Plan Exercise")
    private UUID id;
    @Schema(description = "Exercise Name")
    private String exerciseName;
    @Schema(description = "Number of Sets to be performed")
    private Integer sets;
    @Schema(description = "Number of Reps per Sets to be performed")
    private Integer reps;
    @Schema(description = "Weights To be lifted, 0 if Body weight")
    private Float weightKg;
    @Schema(description = "No of seconds should be taken rest in between sets and exercises")
    private Integer restSeconds;
    @Schema(description = "Order index and must be unique")
    private Integer orderIndex;
    @Schema(description = "Any Instructions which are useful")
    private String notes;
}
