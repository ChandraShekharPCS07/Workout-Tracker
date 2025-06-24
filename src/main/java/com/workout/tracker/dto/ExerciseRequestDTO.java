package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequestDTO {

    @NotBlank(message = "Exercise name must not be blank")
    @Schema(description = "Name of the exercise", example = "Push-Up", required = true)
    private String name;

    @Schema(description = "Optional description of the exercise", example = "A bodyweight exercise for upper body strength")
    private String description;

    @NotBlank(message = "Category must not be blank")
    @Schema(description = "Category like strength, cardio, etc.", example = "Strength", required = true)
    private String category;

    @NotBlank(message = "Muscle group must not be blank")
    @Schema(description = "Targeted muscle group", example = "Chest", required = true)
    private String muscleGroup;
}

