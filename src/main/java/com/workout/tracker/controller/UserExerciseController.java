package com.workout.tracker.controller;

import com.workout.tracker.dto.ExerciseResponseDTO;
import com.workout.tracker.dto.ExerciseSummaryDTO;
import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.service.ExerciseService;
import com.workout.tracker.util.StandardApiErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exercises")
public class UserExerciseController {

    private final ExerciseService exerciseService;

    @StandardApiErrors
    @Operation(summary = "Get all exercises", responses = {
            @ApiResponse(responseCode = "200", description = "Exercise list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExerciseSummaryDTO.class))))
    })
    @GetMapping()
    public ResponseEntity<PagedResponse<ExerciseSummaryDTO>> getAllExercises(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(exerciseService.getAllExercises(page, size));
    }

    @StandardApiErrors
    @Operation(summary = "Get exercises by category", responses = {
            @ApiResponse(responseCode = "200", description = "Exercises list by category",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExerciseSummaryDTO.class))))
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<PagedResponse<ExerciseSummaryDTO>> getAllExercisesByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(exerciseService.getExercisesByCategory(category, page, size));
    }

    @StandardApiErrors
    @Operation(summary = "Get exercises by muscle group", responses = {
            @ApiResponse(responseCode = "200", description = "Exercises list by muscle group",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExerciseSummaryDTO.class))))
    })
    @GetMapping("/muscle-group/{muscleGroup}")
    public ResponseEntity<PagedResponse<ExerciseSummaryDTO>> getAllExercisesByMuscleGroup(
            @PathVariable String muscleGroup,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(exerciseService.getExercisesByMuscleGroup(muscleGroup, page, size));
    }

    @StandardApiErrors
    @Operation(summary = "Get exercise by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Exercise found",
                    content = @Content(schema = @Schema(implementation = ExerciseResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(@PathVariable UUID id) {
        return ResponseEntity.ok(exerciseService.getExerciseById(id));
    }


}
