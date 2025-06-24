package com.workout.tracker.kafka;

import com.workout.tracker.dto.WorkoutScheduleEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class WorkoutScheduleEventListener {

    @KafkaListener(topics = "workout-schedule-events", groupId = "workout-app-consumer")
    public void handleScheduleEvent(WorkoutScheduleEvent event) {
        System.out.println("📥 Received event: " + event.getEventType() + " for schedule " + event.getScheduleId());
        // Future: trigger notification, log entry, etc.
    }
}

