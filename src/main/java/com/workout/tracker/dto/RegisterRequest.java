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
public class RegisterRequest {

    @Schema(description = "User's email address", example = "chandra@example.com")
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    String email;

    @Schema(description = "User's username", example = "chandrapcs")
    @NotBlank(message = "Username is required")
    String username;

    @Schema(description = "User's password", example = "My$3cr3tP@ss")
    @NotBlank(message = "Password is required")
    String password;
}
