package com.workout.tracker.repository;

import com.workout.tracker.model.WorkoutPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {
    Optional<WorkoutPlan> findByNameAndUserUsername(String name, String username);

    @Override
    @EntityGraph(attributePaths = {"exerciseList.exercise"})
    Optional<WorkoutPlan> findById(UUID id);

    Page<WorkoutPlan> findByUserUsername(String username, Pageable pageable);
    Page<WorkoutPlan> findByNameContainingIgnoreCaseAndUserUsername(String name, String username, Pageable pageable);
}
