package com.workout.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogsFilterRequestDTO {

    private UUID workoutScheduleId;

    private UUID exerciseId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Float minWeight;

    private Float maxWeight;

    private String notesQuery;
}

