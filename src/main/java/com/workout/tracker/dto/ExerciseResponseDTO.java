package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseResponseDTO {

    @Schema(description = "Unique identifier of the exercise")
    private UUID id;

    @Schema(description = "Name of the exercise")
    private String name;

    @Schema(description = "Detailed description of the exercise")
    private String description;

    @Schema(description = "Category like strength, cardio, etc.")
    private String category;

    @Schema(description = "Targeted muscle group")
    private String muscleGroup;

    @Schema(description = "Timestamp when the exercise was created")
    private LocalDateTime createdAt;
}

