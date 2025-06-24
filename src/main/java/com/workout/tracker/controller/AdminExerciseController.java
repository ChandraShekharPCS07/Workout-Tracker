package com.workout.tracker.controller;

import com.workout.tracker.dto.ExerciseRequestDTO;
import com.workout.tracker.dto.ExerciseResponseDTO;
import com.workout.tracker.exception.APIResponse;
import com.workout.tracker.service.impl.ExerciseServiceImpl;
import com.workout.tracker.util.StandardApiErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/exercises")
@PreAuthorize("hasRole('ADMIN')")
public class AdminExerciseController {

    private final ExerciseServiceImpl exerciseService;

    @StandardApiErrors
    @Operation(summary = "Create a new exercise", responses = {
            @ApiResponse(responseCode = "201", description = "Exercise created",
                    content = @Content(schema = @Schema(implementation = ExerciseResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<APIResponse<ExerciseResponseDTO>> createExercise(
            @Valid @RequestBody ExerciseRequestDTO exerciseRequestDTO,
            HttpServletRequest request) {

        ExerciseResponseDTO dto = exerciseService.createExercise(exerciseRequestDTO);
        APIResponse<ExerciseResponseDTO> response = APIResponse.success(
                "Exercise created",
                dto,
                HttpStatus.CREATED.value(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @StandardApiErrors
    @Operation(summary = "Update existing exercise", responses = {
            @ApiResponse(responseCode = "200", description = "Exercise updated",
                    content = @Content(schema = @Schema(implementation = ExerciseResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> updateExercise(
            @PathVariable UUID id,
            @Valid @RequestBody ExerciseRequestDTO exerciseRequestDTO) {
        return ResponseEntity.ok(exerciseService.updateExercise(id, exerciseRequestDTO));
    }

    @StandardApiErrors
    @Operation(summary = "Delete exercise", responses = {
            @ApiResponse(responseCode = "204", description = "Exercise deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable UUID id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }


}
