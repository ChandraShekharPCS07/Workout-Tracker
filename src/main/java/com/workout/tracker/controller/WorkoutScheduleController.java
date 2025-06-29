package com.workout.tracker.controller;


import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.dto.WorkoutScheduleRequestDTO;
import com.workout.tracker.dto.WorkoutScheduleResponseDTO;
import com.workout.tracker.dto.WorkoutScheduleSummaryDTO;
import com.workout.tracker.exception.APIResponse;
import com.workout.tracker.model.Status;
import com.workout.tracker.service.impl.WorkoutScheduleServiceImpl;
import com.workout.tracker.util.SecurityUtils;
import com.workout.tracker.util.StandardApiErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/users/scheduled-workouts")
@PreAuthorize("hasRole('USER')")
public class WorkoutScheduleController {

    private final WorkoutScheduleServiceImpl workoutScheduleService;

    private static final Logger log = LoggerFactory.getLogger(WorkoutScheduleController.class);

    @StandardApiErrors
    @Operation(summary = "Get Pending / Completed Workout Schedules", responses = {
            @ApiResponse(responseCode = "200", description = "Workout Schedules",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkoutScheduleSummaryDTO.class))))
    })
    @GetMapping("/status")
    public ResponseEntity<APIResponse<PagedResponse<WorkoutScheduleSummaryDTO>>> getWorkoutSchedulesByStatus(
            @RequestParam Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ){
        String username = SecurityUtils.getCurrentUsername();
        PagedResponse<WorkoutScheduleSummaryDTO> dtoPagedResponse =
                workoutScheduleService.listWorkoutSchedulesByStatus(username, status, page, size);
        log.info("Get {} Workout Schedules for User: {}", status.name(), username);
        APIResponse<PagedResponse<WorkoutScheduleSummaryDTO>> response = APIResponse.success(
                "Workout Schedules",
                dtoPagedResponse,
                HttpStatus.OK.value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @StandardApiErrors
    @Operation(summary = "Get all Workout Schedules", responses = {
            @ApiResponse(responseCode = "200", description = "Workout Schedules",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkoutScheduleSummaryDTO.class))))
    })
    @GetMapping
    public ResponseEntity<PagedResponse<WorkoutScheduleSummaryDTO>> getAllWorkoutSchedules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Get all Workout Schedules for User: {}", username);
        return ResponseEntity.ok().body(workoutScheduleService.listWorkoutSchedules(username, page, size));
    }

    @StandardApiErrors
    @Operation(summary = "Search Workout Schedules", responses = {
            @ApiResponse(responseCode = "200", description = "Workout Schedules",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkoutScheduleSummaryDTO.class))))
    })
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<WorkoutScheduleSummaryDTO>> getWorkoutSchedules(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Search Workout Schedules for User: {}", username);
        return ResponseEntity.ok().body(workoutScheduleService.searchWorkoutSchedules(username, query, page, size));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<WorkoutScheduleResponseDTO> completeWorkoutSchedule(
            @PathVariable("id") UUID workoutScheduleId,
            @RequestParam(value = "notes", required = false) String notes
    ) {
        String username = SecurityUtils.getCurrentUsername();

        WorkoutScheduleResponseDTO completed = workoutScheduleService
                .completeWorkoutSchedule(username, workoutScheduleId, notes);

        return ResponseEntity.ok(completed);
    }

    @StandardApiErrors
    @Operation(summary = "Get Workout Schedule by Id", responses = {
            @ApiResponse(responseCode = "200", description = "Get Workout Schedule by Id",
            content = @Content(schema = @Schema(implementation = WorkoutScheduleResponseDTO.class)))
    })
    @GetMapping("/{workoutScheduleId}")
    public ResponseEntity<WorkoutScheduleResponseDTO> getWorkoutScheduleById(
            @PathVariable UUID workoutScheduleId){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Get Workout Schedule by Id: {} for User: {}", workoutScheduleId, username);
        return ResponseEntity.ok().body(workoutScheduleService.getWorkoutScheduleById(username, workoutScheduleId));
    }


    @StandardApiErrors
    @Operation(summary = "Create Workout Schedule by Id", responses = {
            @ApiResponse(responseCode = "201", description = "Create Workout Schedule by Id",
                    content = @Content(schema = @Schema(implementation = WorkoutScheduleResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<WorkoutScheduleResponseDTO> createWorkoutSchedule(
            @RequestBody WorkoutScheduleRequestDTO requestDTO
    ){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Create Workout Schedule for User: {}", username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutScheduleService.createWorkoutSchedule(username, requestDTO));
    }

    @StandardApiErrors
    @Operation(summary = "Update Workout Schedule by Id", responses = {
            @ApiResponse(responseCode = "200", description = "Update Workout Schedule by Id",
                    content = @Content(schema = @Schema(implementation = WorkoutScheduleResponseDTO.class)))
    })
    @PutMapping("/{workoutScheduleId}")
    public ResponseEntity<WorkoutScheduleResponseDTO> udpateWorkoutScheduleById(
            @PathVariable UUID workoutScheduleId,
            @RequestBody WorkoutScheduleRequestDTO requestDTO
    ){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Update Workout Schedule by Id: {} for User: {}", workoutScheduleId, username);
        return ResponseEntity.ok().body(workoutScheduleService.updateWorkoutSchedule(username, workoutScheduleId, requestDTO));
    }

    @StandardApiErrors
    @Operation(summary = "Get Workout Schedule by Id", responses = {
            @ApiResponse(responseCode = "204", description = "Delete Workout Schedule by Id")
    })
    @DeleteMapping("/{workoutScheduleId}")
    public ResponseEntity<Void> deleteWorkoutScheduleById(
            @PathVariable UUID workoutScheduleId
    ){
        String username = SecurityUtils.getCurrentUsername();
        log.info("Delete Workout Schedule of Id: {} for User: {}",workoutScheduleId, username);
        workoutScheduleService.deleteWorkoutSchedule(username, workoutScheduleId);
        return ResponseEntity.ok().build();
    }
}
