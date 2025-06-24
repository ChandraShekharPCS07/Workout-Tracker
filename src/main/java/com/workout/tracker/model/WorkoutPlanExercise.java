package com.workout.tracker.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "workout_plan_exercise")
public class WorkoutPlanExercise {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "workout_plan_id")
    @JsonBackReference
    private WorkoutPlan workoutPlan;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @JsonBackReference
    private Exercise exercise;

    @Column
    private Integer sets;

    @Column
    private Integer reps;

    @Column
    private Float weightKg;

    @Column
    private Integer restSeconds;

    @Column
    private Integer orderIndex;

    @Column(columnDefinition = "Text")
    private String notes;

}
