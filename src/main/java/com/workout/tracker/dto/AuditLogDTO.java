package com.workout.tracker.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLogDTO {
    private String actionType;
    private String targetType;
    private String metadata;
    private LocalDateTime timestamp;
}

