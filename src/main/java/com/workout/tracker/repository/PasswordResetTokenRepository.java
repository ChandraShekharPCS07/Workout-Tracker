package com.workout.tracker.repository;

import com.workout.tracker.model.PasswordResetToken;
import com.workout.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByUser(User user);
}
