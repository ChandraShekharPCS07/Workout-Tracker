package com.workout.tracker.dto;

import com.workout.tracker.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDto {

    @Size(max = 50)
    private String username;

    @Email
    @Size(max = 100)
    private String email;

    private String displayName;

    private String phoneNumber;

    private Role role;

    private Boolean enabled;
}


