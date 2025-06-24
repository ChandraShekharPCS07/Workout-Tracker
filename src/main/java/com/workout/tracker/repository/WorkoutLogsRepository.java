package com.workout.tracker.repository;

import com.workout.tracker.model.WorkoutLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkoutLogsRepository extends JpaRepository<WorkoutLogs, UUID> {
    Page<WorkoutLogs> findAllByWorkoutScheduleUserUsername(String username, Pageable pageable);
}
