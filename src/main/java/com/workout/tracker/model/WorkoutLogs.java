package com.workout.tracker.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "workout_logs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"workout_schedule_id", "exercise_id"})
)
public class WorkoutLogs {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "workout_schedule_id")
    @JsonBackReference
    private WorkoutSchedule workoutSchedule;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @JsonBackReference
    private Exercise exercise;

    @Column
    private Integer actualSets;

    @Column
    private Integer actualReps;

    @Column
    private Float actualWeightKg;

    @Column(columnDefinition = "Text")
    private String notes;

}
