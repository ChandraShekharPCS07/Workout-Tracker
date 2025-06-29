package com.workout.tracker.dto;

import com.workout.tracker.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private UUID id;

    private String username;

    private String email;

    private boolean enabled;

    private Role role;

    private String displayName;

    private String phoneNumber;

    private Instant createdAt;

    private Instant updatedAt;
}


