package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanExerciseRequestDTO {
    @Schema(description = "Exercise Name that you want to perform", example = "Push_Up")
    @NotBlank(message = "Exercise name must included")
    private String exerciseName;

    @Schema(description = "Sets you want to perform of that exercise", example = "2")
    @NotBlank(message = "Sets are must")
    private Integer sets;

    @Schema(description = "Reps you want to perform in that sets", example = "15")
    @NotBlank(message = "Reps are must")
    private Integer reps;

    @Schema(description = "Weights Like Dumbles and more", example = "50")
    @NotBlank(message = "Weights you want lift, 0 if BodyWeight")
    private Float weightKg;

    @Schema(description = "Rest Seconds in between Exercise", example = "20")
    @NotBlank(message = "Rest Is Important, It is must for recovery of muscles")
    private Integer restSeconds;

    @Schema(description = "Order Index", example = "1")
    @NotBlank(message = "Order Index is must and unique")
    private Integer orderIndex;

    @Schema(description = "Instructions")
    private String notes;
}
