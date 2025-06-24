package com.workout.tracker.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WorkoutScheduleEvent {
    private UUID scheduleId;
    private String username;
    private String workoutPlanName;
    private LocalDateTime scheduledAt;
    private String eventType; // "CREATED", "COMPLETED", etc.
}

