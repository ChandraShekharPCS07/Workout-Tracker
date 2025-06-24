package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanSummaryDTO {
    @Schema(description = "Unique Identifier of WorkoutPlan")
    private UUID id;
    @Schema(description = "Name of workout plan")
    private String name;
}
