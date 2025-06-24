package com.workout.tracker.service.impl;

import com.workout.tracker.annotations.RateLimited;
import com.workout.tracker.dto.WorkoutScheduleEvent;
import com.workout.tracker.dto.WorkoutScheduleRequestDTO;
import com.workout.tracker.dto.WorkoutScheduleResponseDTO;
import com.workout.tracker.dto.WorkoutScheduleSummaryDTO;
import com.workout.tracker.exception.CustomAccessDeniedException;
import com.workout.tracker.exception.UserNotFoundException;
import com.workout.tracker.exception.WorkoutScheduleNotFoundException;
import com.workout.tracker.kafka.KafkaEventPublisher;
import com.workout.tracker.mappers.WorkoutScheduleMapper;
import com.workout.tracker.model.*;
import com.workout.tracker.repository.UserRepository;
import com.workout.tracker.repository.WorkoutLogsRepository;
import com.workout.tracker.repository.WorkoutPlanRepository;
import com.workout.tracker.repository.WorkoutScheduleRepository;
import com.workout.tracker.service.WorkoutScheduleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutScheduleServiceImpl implements WorkoutScheduleService {

    private final KafkaEventPublisher kafkaEventPublisher;
    private final WorkoutScheduleRepository workoutScheduleRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutScheduleMapper workoutScheduleMapper;
    private final UserRepository userRepository;
    private final WorkoutLogsRepository workoutLogsRepository;

    @Override
    @Transactional(readOnly = true) // ✅ Commandment 3
    public List<WorkoutScheduleSummaryDTO> getAllUpcomingWorkoutSchedulesByUsername(String username) {
        // 🎯 PURPOSE: Fetch all future workouts for a user

        // 🛡️ PRE-CHECK
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be null or blank");
        }

        // 🔧 PROCESSING
        List<WorkoutSchedule> upcomingSchedules = workoutScheduleRepository
                .findAllByUserUsernameAndScheduledAtAfter(username, LocalDateTime.now());

        // 🌈 POST-PROCESSING
        return upcomingSchedules.stream()
                .map(workoutScheduleMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutScheduleSummaryDTO> getAllCompletedWorkoutSchedulesByUsername(String username) {
        // 🎯 PURPOSE: Fetch all past workouts for a user

        // 🛡️ PRE-CHECK
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be null or blank");
        }

        // 🔧 PROCESSING
        List<WorkoutSchedule> completedSchedules = workoutScheduleRepository
                .findAllByUserUsernameAndScheduledAtBefore(username, LocalDateTime.now());

        // 🌈 POST-PROCESSING
        return completedSchedules.stream()
                .map(workoutScheduleMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    @RateLimited
    public WorkoutScheduleResponseDTO getWorkoutScheduleById(String username, UUID workoutScheduleId) {
        // 🎯 PURPOSE: Retrieve full details of a workout schedule for the given user

        // 🛡️ PRE-CHECK
        if (workoutScheduleId == null) {
            throw new IllegalArgumentException("WorkoutSchedule ID must not be null");
        }

        // 🔐 Ownership + existence check
        WorkoutSchedule workoutSchedule = checkOwnership(username, workoutScheduleId);

        // 🌈 POST-PROCESSING
        return workoutScheduleMapper.toResponseDTO(workoutSchedule);
    }


    @Override
    @Transactional // 💾 Commandment 3: Only when writing to the DB
    public WorkoutScheduleResponseDTO createWorkoutSchedule(String username, WorkoutScheduleRequestDTO requestDTO) {

        // 1. 🎯 PURPOSE
        // Create a new workout schedule for the given user, associated with a named workout plan.

        // 2. 🛡️ PRE-CHECKS
        if (requestDTO == null || requestDTO.getWorkoutPlanName() == null || requestDTO.getWorkoutPlanName().isBlank()) {
            throw new IllegalArgumentException("Workout plan name must not be null or blank"); // ✅ Commandment 2
        }

        // Check that the workout plan exists and belongs to the user
        WorkoutPlan workoutPlan = workoutPlanRepository
                .findByNameAndUserUsername(requestDTO.getWorkoutPlanName(), username)
                .orElseThrow(() -> new WorkoutScheduleNotFoundException("Workout plan not found")); // ✅ Commandment 5

        // Confirm the user exists
        User user = findUserByUsername(username); // 🛡️ Ownership check (Commandment 17)

        // 3. 🔧 PROCESSING
        WorkoutSchedule workoutSchedule = workoutScheduleMapper.toEntity(requestDTO);
        workoutSchedule.setUser(user);
        workoutSchedule.setWorkoutPlan(workoutPlan); // 🔧 Ensure relation integrity

        // 4. 💾 PERSISTENCE
        WorkoutSchedule saved = workoutScheduleRepository.save(workoutSchedule);

        // ✅ Emit Kafka event
        WorkoutScheduleEvent event = new WorkoutScheduleEvent();
        event.setScheduleId(saved.getId());
        event.setUsername(username);
        event.setWorkoutPlanName(requestDTO.getWorkoutPlanName());
        event.setScheduledAt(saved.getScheduledAt());
        event.setEventType("CREATED");

        kafkaEventPublisher.publishWorkoutScheduleEvent(event);

        // 5. 🌈 POST-PROCESSING / OUTPUT
        return workoutScheduleMapper.toResponseDTO(saved); // ✅ Commandment 7
    }


    @Override
    @Transactional // 💾 Commandment 3: Necessary for update/write operations
    public WorkoutScheduleResponseDTO updateWorkoutSchedule(
            String username,
            UUID workoutScheduleId,
            WorkoutScheduleRequestDTO requestDTO
    ) {
        // 1. 🎯 PURPOSE
        // Update a user's workout schedule by ID.

        // 2. 🛡️ PRE-CHECKS
        if (requestDTO == null) {
            throw new IllegalArgumentException("Request body cannot be null"); // ✅ Commandment 2
        }

        if (workoutScheduleId == null) {
            throw new IllegalArgumentException("Workout schedule ID must not be null");
        }

        // Check ownership and existence
        WorkoutSchedule existing = checkOwnership(username, workoutScheduleId); // ✅ Commandments 2, 5, 17

        // 3. 🔧 PROCESSING
        WorkoutSchedule updated = workoutScheduleMapper.updateFromDTO(existing, requestDTO); // 🔧 Clean business logic

        // 4. 💾 PERSISTENCE
        WorkoutSchedule saved = workoutScheduleRepository.save(updated);

        WorkoutScheduleEvent event = new WorkoutScheduleEvent();
        event.setScheduleId(saved.getId());
        event.setUsername(username);
        event.setWorkoutPlanName(requestDTO.getWorkoutPlanName());
        event.setScheduledAt(saved.getScheduledAt());
        event.setEventType("UPDATED");

        // 5. 🌈 POST-PROCESSING / OUTPUT
        return workoutScheduleMapper.toResponseDTO(saved); // ✅ Commandment 7
    }

    // 1. 🎯 PURPOSE
    @Override
    @Transactional // 💾 Commandment 3: Required for deletion
    public void deleteWorkoutSchedule(String username, UUID workoutScheduleId) {
        // 1. 🎯 PURPOSE
        // Delete a workout schedule that belongs to the given user

        // 2. 🛡️ PRE-CHECKS
        if (workoutScheduleId == null) {
            throw new IllegalArgumentException("WorkoutSchedule ID must not be null"); // ✅ Commandment 2
        }

        // 🔐 Ownership check also ensures existence
        WorkoutSchedule workoutSchedule = checkOwnership(username, workoutScheduleId); // ✅ Commandments 5, 17

        // 3. 🔧 PROCESSING
        // No additional business logic, but could include: prevent deletion if already completed

        // 4. 💾 PERSISTENCE
        workoutScheduleRepository.deleteById(workoutScheduleId); // ✅ Safe due to prior existence check

        // 5. 🌈 POST-PROCESSING
        // Optional: emit event or log action
        // log.info("User {} deleted workout schedule {}", username, workoutScheduleId);
    }




    @Override
    @Transactional // 💾 Commandment 3: Needed for DB writes
    public WorkoutScheduleResponseDTO completeWorkoutSchedule(
            String username,
            UUID workoutScheduleId,
            String notes
    ) {
        // 1. 🎯 PURPOSE
        // Mark a workout schedule as completed with optional notes.

        // 2. 🛡️ PRE-CHECKS

        if (workoutScheduleId == null) {
            throw new IllegalArgumentException("WorkoutSchedule ID must not be null"); // ✅ Commandment 2
        }

        // 🔐 Ownership & existence check
        WorkoutSchedule workoutSchedule = checkOwnership(username, workoutScheduleId); // ✅ Commandment 17

        // ⏮️ Idempotency check — if already completed, return existing
        if (Status.COMPLETED.equals(workoutSchedule.getStatus())) {
            return workoutScheduleMapper.toResponseDTO(workoutSchedule); // ✅ Commandment 12
        }

        // ⏰ Domain constraint: can't complete a future workout
        if (workoutSchedule.getScheduledAt().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot complete a workout scheduled in the future."); // ✅ Commandment 9
        }

        // 3. 🔧 PROCESSING
        workoutSchedule.setStatus(Status.COMPLETED);
        workoutSchedule.setCompletedAt(LocalDateTime.now());
        workoutSchedule.setNotes(notes); // null-safe — set even if null

        // 4. 💾 PERSISTENCE
        WorkoutSchedule saved = workoutScheduleRepository.save(workoutSchedule);

        // AUTO-CREATE WORKOUT LOGS
        List<WorkoutLogs> logs = Optional.ofNullable(saved.getWorkoutPlan())
                .map(WorkoutPlan::getExerciseList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(exercisePlan -> exercisePlan.getExercise() != null) // Defensive: avoid null pointer
                .map(exercisePlan -> {
                    WorkoutLogs log = new WorkoutLogs();
                    log.setWorkoutSchedule(saved);
                    log.setExercise(exercisePlan.getExercise());
                    log.setActualSets(exercisePlan.getSets() != null ? exercisePlan.getSets() : 0);
                    log.setActualReps(exercisePlan.getReps() != null ? exercisePlan.getReps() : 0);
                    log.setActualWeightKg(exercisePlan.getWeightKg() != null ? exercisePlan.getWeightKg() : 0f);
                    log.setNotes(saved.getNotes()); // Optional: can be null or general notes
                    return log;
                })
                .toList();

        workoutLogsRepository.saveAll(logs);


        WorkoutScheduleEvent event = new WorkoutScheduleEvent();
        event.setScheduleId(saved.getId());
        event.setUsername(username);
        event.setWorkoutPlanName(saved.getWorkoutPlan().getName());
        event.setScheduledAt(saved.getScheduledAt());
        event.setEventType("COMPLETED");

        // 5. 🌈 POST-PROCESSING / OUTPUT
        return workoutScheduleMapper.toResponseDTO(saved); // ✅ Commandment 7
    }


    private WorkoutSchedule findWorkoutScheduleById(UUID id){
        return workoutScheduleRepository.findById(id)
                .orElseThrow(() -> new WorkoutScheduleNotFoundException("Workout schedule not found"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private WorkoutSchedule checkOwnership(String username, UUID scheduleId) {
        WorkoutSchedule schedule = workoutScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new WorkoutScheduleNotFoundException("Workout schedule not found")); // ✅ Known exception

        if (!schedule.getUser().getUsername().equals(username)) {
            throw new CustomAccessDeniedException("You are not authorized to modify this workout schedule"); // ✅ Clear access check
        }

        return schedule;
    }


}
