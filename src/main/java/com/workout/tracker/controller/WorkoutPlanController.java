package com.workout.tracker.controller;

import com.workout.tracker.dto.WorkoutPlanRequestDTO;
import com.workout.tracker.dto.WorkoutPlanResponseDTO;
import com.workout.tracker.dto.WorkoutPlanSummaryDTO;
import com.workout.tracker.dto.WorkoutScheduleSummaryDTO;
import com.workout.tracker.service.impl.WorkoutPlanServiceImpl;
import com.workout.tracker.util.SecurityUtils;
import com.workout.tracker.util.StandardApiErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
@RequestMapping("api/v1/users/workout-plans")
@PreAuthorize("hasRole('USER')")
public class WorkoutPlanController {

    private final WorkoutPlanServiceImpl workoutPlanService;

    private static final Logger log = LoggerFactory.getLogger(WorkoutPlanController.class);

    @StandardApiErrors
    @Operation(summary = "Get all Workout Plans", responses = {
            @ApiResponse(responseCode = "200", description = "Workout Plans List",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkoutScheduleSummaryDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<WorkoutPlanSummaryDTO>> getAllWorkoutPlans(){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Fetching workout plans for user: {}", username);
        return ResponseEntity.ok().body(workoutPlanService.getAllWorkoutPlans(username));
    }

    @StandardApiErrors
    @Operation(summary = "Get workout plan by id", responses = {
            @ApiResponse(responseCode = "200", description = "Workout Plan",
            content = @Content(schema = @Schema(implementation = WorkoutPlanResponseDTO.class)))
    })
    @GetMapping("/{workoutPlanId}")
    public ResponseEntity<WorkoutPlanResponseDTO> getWorkoutPlanById(@PathVariable UUID workoutPlanId){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Fetching workout plan by id for user: {}", username);
        return ResponseEntity.ok().body(workoutPlanService.getWorkoutPlanById(username, workoutPlanId));
    }

    @StandardApiErrors
    @Operation(summary = "Create workout plan for user", responses = {
            @ApiResponse(responseCode = "201", description = "Create workout plan",
            content = @Content(schema = @Schema(implementation = WorkoutPlanResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<WorkoutPlanResponseDTO> createWorkoutPlan(@RequestBody WorkoutPlanRequestDTO requestDTO){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Creating new workout plan for user: {}", username);
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutPlanService.createWorkoutPlan(username, requestDTO));
    }

    @StandardApiErrors
    @Operation(summary = "Update workout plan for user", responses = {
            @ApiResponse(responseCode = "200", description = "Update workout plan",
            content = @Content(schema = @Schema(implementation = WorkoutPlanResponseDTO.class)))
    })
    @PutMapping("/{workoutPlanId}")
    public ResponseEntity<WorkoutPlanResponseDTO> updateWorkoutPlanById(@PathVariable UUID workoutPlanId,@Valid @RequestBody WorkoutPlanRequestDTO requestDTO){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Updating workout plan for user: {}", username);
        return ResponseEntity.status(HttpStatus.OK).body(workoutPlanService.updateWorkoutPlan(username, workoutPlanId, requestDTO));
    }

    @StandardApiErrors
    @Operation(summary = "Delete workout plan for user", responses = {
            @ApiResponse(responseCode = "204", description = "Delete workout plan")
    })
    @DeleteMapping("/{workoutPlanId}")
    public ResponseEntity<Void> deleteWorkoutPlanById(@PathVariable UUID workoutPlanId){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Deleting workout plan for user: {}", username);
        workoutPlanService.deleteWorkoutPlan(username, workoutPlanId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
