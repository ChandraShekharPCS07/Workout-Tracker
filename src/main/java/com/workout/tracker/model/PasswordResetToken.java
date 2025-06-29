package com.workout.tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PasswordResetToken {
    @Id
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant expiryDate;
}

