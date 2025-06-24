package com.workout.tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WorkoutPlanExerciseNotFoundException extends RuntimeException {
    public WorkoutPlanExerciseNotFoundException(String message) {
        super(message);
    }
}
