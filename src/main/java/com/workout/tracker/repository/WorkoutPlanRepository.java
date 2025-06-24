package com.workout.tracker.repository;

import com.workout.tracker.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {
    List<WorkoutPlan> findAllByUserUsername(String username);
    Optional<WorkoutPlan> findByNameAndUserUsername(String name, String username);

    @Query("SELECT wp FROM WorkoutPlan wp LEFT JOIN FETCH wp.exerciseList el LEFT JOIN FETCH el.exercise WHERE wp.id = :planId")
    Optional<WorkoutPlan> findByIdWithExercises(@Param("planId") UUID id);

}
