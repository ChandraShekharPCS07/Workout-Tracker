package com.workout.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogsSummaryDTO {

    private UUID id;

    private String exerciseName;

    private Float actualWeightKg;

    private Integer actualSets;

    private Integer actualReps;

    private String notes;
}

