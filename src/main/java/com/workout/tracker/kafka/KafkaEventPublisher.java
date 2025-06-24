package com.workout.tracker.kafka;

import com.workout.tracker.dto.WorkoutScheduleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, WorkoutScheduleEvent> kafkaTemplate;

    public void publishWorkoutScheduleEvent(WorkoutScheduleEvent event) {
        kafkaTemplate.send("workout-schedule-events", event);
    }
}

