package com.workout.tracker.repository;

import com.workout.tracker.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUsernameOrderByTimestampDesc(String username);
}
