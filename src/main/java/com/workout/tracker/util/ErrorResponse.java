package com.workout.tracker.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@Schema(description = "Standard API error response")
public class ErrorResponse {

    @Schema(example = "400")
    private final int code;

    @Schema(example = "false")
    private final boolean success;

    @Schema(example = "Validation failed")
    private final String message;

    @Schema(example = "[\"email must not be null\"]")
    private final List<String> errors;

    @Schema(example = "/api/v1/users")
    private final String path;

    @Schema(example = "2025-06-22T10:35:12.000Z")
    private final Instant timestamp;
}