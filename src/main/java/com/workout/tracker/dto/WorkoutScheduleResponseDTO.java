package com.workout.tracker.dto;

import com.workout.tracker.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutScheduleResponseDTO {
    @Schema(description = "Unique Identifier for Wrokout Schedule Response")
    private UUID id;
    @Schema(description = "Workout plan name")
    private String workoutPlanName;
    @Schema(description = "Workout Schedule At")
    private LocalDateTime scheduledAt;
    @Schema(description = "Workout Schedule status")
    private Status status;
    @Schema(description = "Workout Completed At")
    private LocalDateTime completedAt;
    @Schema(description = "Wokrout duration time")
    private Integer durationMins;
    @Schema(description = "User review")
    private String notes;
}
