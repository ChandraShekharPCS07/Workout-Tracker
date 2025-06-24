package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseSummaryDTO {

    @Schema(description = "Unique identifier of the exercise", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Name of the exercise", example = "Push-Up")
    private String name;

    @Schema(description = "Category like strength, cardio, etc.", example = "Strength")
    private String category;
}

