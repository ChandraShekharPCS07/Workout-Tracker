package com.workout.tracker.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * A generic API response wrapper for HTTP responses.
 * Contains standard fields such as status code, success flag, message, response data, timestamp, etc.
 *
 * @param <T> The type of data returned in the response.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int code;
    private final boolean success;
    private final String message;
    private final T data;
    private final String token;
    private final Instant timestamp;
    private final List<String> errors;
    private final String path;

    @Builder
    public APIResponse(int code,
                       boolean success,
                       String message,
                       T data,
                       String token,
                       List<String> errors,
                       String path) {

        // Optional: runtime validation (uncomment if needed)
        // if (message == null || path == null) {
        //     throw new IllegalArgumentException("Message and path must not be null.");
        // }

        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
        this.token = token;
        this.errors = errors;
        this.path = path;
        this.timestamp = Instant.now();
    }

    /**
     * Creates a basic success response.
     */
    public static <T> APIResponse<T> success(String message, T data, int code, String path) {
        return APIResponse.<T>baseBuilder(code, true, message, path)
                .data(data)
                .build();
    }

    /**
     * Creates a basic error response.
     */
    public static <T> APIResponse<T> error(String message, int code, String path) {
        return APIResponse.<T>baseBuilder(code, false, message, path).build();
    }

    /**
     * Creates an error response with detailed field-level errors.
     */
    public static <T> APIResponse<T> error(String message, int code, String path, List<String> errors) {
        return APIResponse.<T>baseBuilder(code, false, message, path)
                .errors(errors)
                .build();
    }

    /**
     * Creates a success response with a JWT token.
     */
    public static <T> APIResponse<T> successWithToken(String message, String token, int code, T data, String path) {
        return APIResponse.<T>baseBuilder(code, true, message, path)
                .token(token)
                .data(data)
                .build();
    }

    /**
     * Creates a success response with multiple tokens in a map.
     */
    public static APIResponse<Map<String, String>> successWithTokens(String message, Map<String, String> tokens, int code, String path) {
        return APIResponse.<Map<String, String>>baseBuilder(code, true, message, path)
                .data(tokens)
                .build();
    }

    /**
     * Shared base builder for all factory methods to reduce duplication.
     */
    private static <T> APIResponseBuilder<T> baseBuilder(int code, boolean success, String message, String path) {
        return APIResponse.<T>builder()
                .code(code)
                .success(success)
                .message(message)
                .path(path);
    }

}

