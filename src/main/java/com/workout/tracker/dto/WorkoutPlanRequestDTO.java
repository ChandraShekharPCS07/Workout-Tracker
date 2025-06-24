package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanRequestDTO {
    @Schema(description = "WorkoutPlan must have name", example = "Chest Workouts")
    @NotNull(message = "Name should be atleast 4 characters")
    @Min(4)
    private String name;

    @Schema(description = "Write some notes about how to perform this exercise")
    private String notes;
}
