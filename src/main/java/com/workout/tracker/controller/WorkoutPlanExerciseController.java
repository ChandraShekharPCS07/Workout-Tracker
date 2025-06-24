package com.workout.tracker.controller;

import com.workout.tracker.dto.WorkoutPlanExerciseRequestDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseResponseDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseSummaryDTO;
import com.workout.tracker.service.impl.WorkoutPlanExerciseServiceImpl;
import com.workout.tracker.util.SecurityUtils;
import com.workout.tracker.util.StandardApiErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users/workout-plans/{workoutPlanId}/exercises")
@PreAuthorize("hasRole('USER')")
public class WorkoutPlanExerciseController {

    private final WorkoutPlanExerciseServiceImpl workoutPlanExerciseService;

    private static final Logger log = LoggerFactory.getLogger(WorkoutPlanExerciseController.class);

    @StandardApiErrors
    @Operation(summary = "Get all exercises list of workout plan", responses = {
            @ApiResponse(responseCode = "200", description = "Exercise List",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkoutPlanExerciseSummaryDTO.class))))
    })
    @GetMapping("/all")
    public ResponseEntity<List<WorkoutPlanExerciseSummaryDTO>> getAllWorkoutPlanExercises(@PathVariable UUID workoutPlanId){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Fetching Exercise List of Workout Plan Id: {} for User {}", workoutPlanId, username);
        return ResponseEntity.ok().body(workoutPlanExerciseService.getAllWorkoutPlanExerciseByWorkoutPlanId(username, workoutPlanId));
    }

    @StandardApiErrors
    @Operation(summary = "Get exercise by id of workout plan", responses = {
            @ApiResponse(responseCode = "200", description = "Exercise by id",
            content = @Content(schema = @Schema(implementation = WorkoutPlanExerciseRequestDTO.class)))    })
    @GetMapping("/all/{workoutPlanExerciseId}")
    public ResponseEntity<WorkoutPlanExerciseResponseDTO> getWorkoutPlanExerciseById(@PathVariable UUID workoutPlanId, @PathVariable UUID workoutPlanExerciseId){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Fetching Exercise By Id: {} of Workout Plan Id: {} for User: {}",workoutPlanExerciseId, workoutPlanId, username);
        return ResponseEntity.ok().body(workoutPlanExerciseService.getWorkoutPlanExerciseById(username, workoutPlanExerciseId, workoutPlanId));
    }

    @StandardApiErrors
    @Operation(summary = "Add exercise in workout plan", responses = {
            @ApiResponse(responseCode = "201", description = "Add Exercise",
            content = @Content(schema = @Schema(implementation = WorkoutPlanExerciseRequestDTO.class)))
    })
    @PostMapping
    public ResponseEntity<WorkoutPlanExerciseResponseDTO> createWorkoutPlanExercise(@PathVariable UUID workoutPlanId, @RequestBody WorkoutPlanExerciseRequestDTO requestDTO){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Adding Exercise in Workout Plan Id: {} for User: {}", workoutPlanId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutPlanExerciseService.createWorkoutPlanExercise(username, requestDTO, workoutPlanId));
    }

    @StandardApiErrors
    @Operation(summary = "update exercise in workout plan", responses = {
            @ApiResponse(responseCode = "200", description = "Update Exercise",
            content = @Content(schema = @Schema(implementation = WorkoutPlanExerciseRequestDTO.class)))
    })
    @PutMapping("/{workoutPlanExerciseId}")
    public ResponseEntity<WorkoutPlanExerciseResponseDTO> updateWorkoutPlanExerciseById(
            @PathVariable UUID workoutPlanId,
            @PathVariable UUID workoutPlanExerciseId,
            @RequestBody WorkoutPlanExerciseRequestDTO requestDTO){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Update Exercise by Id: {} of Workout Plan Id: {} for User: {}",
                workoutPlanExerciseId, workoutPlanId, username);
        return ResponseEntity.ok().body(workoutPlanExerciseService
                .updateWorkoutPlanExercise(username, workoutPlanExerciseId, requestDTO, workoutPlanId));
    }


    @StandardApiErrors
    @Operation(summary = "Delete Exercise by Id", responses = {
            @ApiResponse(responseCode = "204", description = "Delete Exercise")
    })
    @DeleteMapping("/{workoutPlanExerciseId}")
    public ResponseEntity<Void> deleteWorkoutPlanExerciseById(
            @PathVariable UUID workoutPlanId,
            @PathVariable UUID workoutPlanExerciseId
    ){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Delete Exercise by Id: {} of Workout Plan Id: {} for User: {}",
                workoutPlanExerciseId, workoutPlanId, username);
        workoutPlanExerciseService.deleteWorkoutPlanExercise(username, workoutPlanExerciseId, workoutPlanId);
        return ResponseEntity.noContent().build();
    }
}
