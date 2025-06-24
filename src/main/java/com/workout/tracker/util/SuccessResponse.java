package com.workout.tracker.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@Schema(description = "Standard API success response")
public class SuccessResponse<T> {
    @Schema(example = "200")
    private final int code;
    @Schema(example = "true")
    private final boolean success;
    @Schema(example = "Data fetched successfully")
    private final String message;
    @Schema(description = "Actual response payload")
    private final T data;
    @Schema(example = "/api/v1/users/1")
    private final String path;
    @Schema(example = "2025-06-22T10:42:00Z")
    private final Instant timestamp;
}
