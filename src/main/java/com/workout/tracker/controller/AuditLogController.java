package com.workout.tracker.controller;

import com.workout.tracker.dto.AuditLogDTO;
import com.workout.tracker.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping("/user/{username}")
    public List<AuditLogDTO> getUserAuditLogs(@PathVariable String username) {
        return auditLogRepository.findByUsernameOrderByTimestampDesc(username)
                .stream()
                .map(log -> {
                    AuditLogDTO dto = new AuditLogDTO();
                    dto.setActionType(log.getActionType());
                    dto.setTargetType(log.getTargetType());
                    dto.setMetadata(log.getMetadata());
                    dto.setTimestamp(log.getTimestamp());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

