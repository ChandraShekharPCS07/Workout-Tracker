package com.workout.tracker.dto;

import com.workout.tracker.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

//    private String refreshToken;

    private UUID userId;

    private String username;

    private String email;

    private Role role;
}

