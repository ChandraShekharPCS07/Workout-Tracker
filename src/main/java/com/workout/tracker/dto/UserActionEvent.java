package com.workout.tracker.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserActionEvent {
    private String username;
    private String actionType;     // e.g., "WORKOUT_COMPLETED", "PLAN_UPDATED"
    private UUID targetId;         // ID of the object affected
    private String targetType;     // e.g., "WorkoutSchedule", "WorkoutPlan"
    private LocalDateTime timestamp;
    private String metadata;       // Optional JSON string or details
}
