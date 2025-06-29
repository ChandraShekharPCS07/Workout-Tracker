package com.workout.tracker.kafka;

import com.workout.tracker.dto.UserActionEvent;
import com.workout.tracker.model.AuditLog;
import com.workout.tracker.repository.AuditLogRepository;
import com.workout.tracker.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserActionAuditListener {

    private final AuditLogRepository auditLogRepository;
    private final EmailService emailService;

    @Value("${audit.persistence.postgres-enabled:true}")
    private boolean writeToPostgres;

    @KafkaListener(
            topics = "${audit.kafka.topic:user-action-events}",
            groupId = "${audit.kafka.group-id:audit-consumer}"
    )
    public void handleUserAction(UserActionEvent event) {
        if (writeToPostgres) {
            AuditLog log = new AuditLog();
            log.setUsername(event.getUsername());
            log.setActionType(event.getActionType());
            log.setTargetId(event.getTargetId());
            log.setTargetType(event.getTargetType());
            log.setTimestamp(event.getTimestamp());
            log.setMetadata(event.getMetadata());
            auditLogRepository.save(log);

            emailService.sendEmail("chandrashekharpcs07@gmail.com", event.getActionType(), event.getUsername());

        }
    }
}


