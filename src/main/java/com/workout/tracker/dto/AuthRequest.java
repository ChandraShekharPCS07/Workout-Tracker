package com.workout.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Schema(description = "User's username", example = "chandrapcs")
    @NotBlank(message = "username is required")
    private String username;

    @Schema(description = "User's password", example = "My$3cr3tP@ss")
    @NotBlank(message = "Password is required")
    private String password;
}
