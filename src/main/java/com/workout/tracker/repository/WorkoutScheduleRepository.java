package com.workout.tracker.repository;

import com.workout.tracker.model.Status;
import com.workout.tracker.model.WorkoutSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface WorkoutScheduleRepository extends JpaRepository<WorkoutSchedule, UUID> {

    Page<WorkoutSchedule> findByUserUsernameOrderByCreatedAtDesc(
            String username,
            Pageable pageable
    );

    Page<WorkoutSchedule> findByUserUsernameAndStatusOrderByCreatedAtDesc(
            String username,
            Status status,
            Pageable pageable
    );

    Page<WorkoutSchedule> findByUserUsernameAndWorkoutPlan_NameContainingIgnoreCaseOrderByCreatedAtDesc(
            String username,
            String workoutPlanName,
            Pageable pageable
    );

    @Query("""
        SELECT ws FROM WorkoutSchedule ws
        WHERE ws.user.username = :username
          AND (:status IS NULL OR ws.status = :status)
          AND (:planName IS NULL OR LOWER(ws.workoutPlan.name) LIKE LOWER(CONCAT('%', :planName, '%')))
    """)
    Page<WorkoutSchedule> searchSchedules(
            @Param("username") String username,
            @Param("status") Status status,
            @Param("planName") String planName,
            Pageable pageable
    );
}

