package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutLogsRequestDTO {
    @Schema(description = "Workout Schedule Id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "workout schedule id is required")
    private UUID workoutScheduleId;

    @Schema(description = "Exercise Name", example = "Push-Up")
    @NotBlank(message = "Exercise must be provided")
    private String exerciseName;

    @Schema(description = "Actual sets done", example = "3")
    @NotNull(message = "actual sets must be provided")
    private Integer actualSets;

    @Schema(description = "Actual Reps per set", example = "10")
    @NotNull(message = "actual reps must be provided")
    private Integer actualReps;

    @Schema(description = "Actual Weight In KG", example = "5")
    @NotNull(message = "Must Provide KG of dumble or any weight, if not weight mark them as 0")
    private Float actualWeightKg;

    @Schema(description = "Write your review what went right, what went wrong, is the exercise easy or hard")
    private String notes;
}
