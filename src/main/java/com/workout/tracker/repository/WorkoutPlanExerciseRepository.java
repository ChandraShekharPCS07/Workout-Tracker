package com.workout.tracker.repository;

import com.workout.tracker.model.WorkoutPlanExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WorkoutPlanExerciseRepository extends JpaRepository<WorkoutPlanExercise, UUID> {
    @Query("SELECT wpe FROM WorkoutPlanExercise wpe JOIN FETCH wpe.exercise WHERE wpe.workoutPlan.id = :id")
    List<WorkoutPlanExercise> getAllByWorkoutPlanId(@Param("id") UUID id);

}
