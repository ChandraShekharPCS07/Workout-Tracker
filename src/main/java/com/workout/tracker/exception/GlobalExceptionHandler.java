package com.workout.tracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<APIResponse<Void>> buildErrorResponse(String message, int status, String uri, List<String> details) {
        APIResponse<Void> response = APIResponse.error(message, status, uri, details);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler({
            ExerciseNotFoundException.class,
            UserNotFoundException.class,
            WorkoutLogsNotFoundException.class,
            WorkoutPlanExerciseNotFoundException.class,
            WorkoutPlanNotFoundException.class,
            WorkoutScheduleNotFoundException.class
    })
    public ResponseEntity<APIResponse<Void>> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(
                ex.getClass().getSimpleName().replace("Exception", "").replaceAll("([a-z])([A-Z])", "$1 $2") + ": " + ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI(),
                List.of(ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());
        return buildErrorResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                errorMessages
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse<Void>> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildErrorResponse(
                "Invalid JSON: " + ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                List.of(ex.getMessage())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<Void>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(
                "Validation error",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                List.of(ex.getMessage())
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<Void>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildErrorResponse(
                "You are not authorized to perform this action.",
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI(),
                List.of("Access Denied")
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception caught:", ex);
        return buildErrorResponse(
                "Something went wrong. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI(),
                List.of(ex.getMessage())
        );
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}
