package com.workout.tracker.repository;

import com.workout.tracker.model.WorkoutSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WorkoutScheduleRepository extends JpaRepository<WorkoutSchedule, UUID> {
    List<WorkoutSchedule> findAllByUserUsernameAndScheduledAtAfter(String username, LocalDateTime now);
    List<WorkoutSchedule> findAllByUserUsernameAndScheduledAtBefore(String username, LocalDateTime now);
}
