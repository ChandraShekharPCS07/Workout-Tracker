package com.workout.tracker.repository;

import com.workout.tracker.model.WorkoutPlanExercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WorkoutPlanExerciseRepository extends JpaRepository<WorkoutPlanExercise, UUID> {
    @Query("SELECT wpe FROM WorkoutPlanExercise wpe WHERE wpe.workoutPlan.user.username = :username")
    Page<WorkoutPlanExercise> findByUserUsername(@Param("username") String username, Pageable pageable);

    @Query("""
        SELECT e FROM WorkoutPlanExercise e
        WHERE e.workoutPlan.user.username = :username
          AND e.workoutPlan.id = :workoutPlanId
          AND (
             LOWER(e.exercise.name) LIKE LOWER(CONCAT('%', :query, '%'))
             OR LOWER(e.notes) LIKE LOWER(CONCAT('%', :query, '%'))
          )
    """)
    Page<WorkoutPlanExercise> searchExercises(
            @Param("username") String username,
            @Param("workoutPlanId") UUID workoutPlanId,
            @Param("query") String query,
            Pageable pageable
    );



}
