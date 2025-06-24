package com.workout.tracker.dto;

import com.workout.tracker.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutScheduleSummaryDTO {
    @Schema(description = "Unique Identifier for Wrokout Schedule Response")
    private UUID id;
    @Schema(description = "Workout plan name")
    private String workoutPlanName;
    @Schema(description = "Workout Schedule At")
    private LocalDateTime scheduledAt;
    @Schema(description = "Workout Schedule status")
    private Status status;
}
