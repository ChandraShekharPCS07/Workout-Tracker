package com.workout.tracker.repository;

import com.workout.tracker.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    Optional<Exercise> findByName(String name);
    Page<Exercise> findAll(Pageable pageable);
    Page<Exercise> findByCategory(String category, Pageable pageable);
    Page<Exercise> findByMuscleGroup(String muscleGroup, Pageable pageable);
    Page<Exercise> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Exercise> findByCategoryIgnoreCaseAndMuscleGroupIgnoreCase(String category, String muscleGroup, Pageable pageable);
}
