package com.workout.tracker.service.impl;

import com.workout.tracker.dto.*;
import com.workout.tracker.exception.CustomAccessDeniedException;
import com.workout.tracker.exception.WorkoutLogsNotFoundException;
import com.workout.tracker.mappers.WorkoutLogsMapper;
import com.workout.tracker.model.WorkoutLogs;
import com.workout.tracker.repository.WorkoutLogsRepository;
import com.workout.tracker.service.WorkoutLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutLogsServiceImpl implements WorkoutLogsService {

    private final WorkoutLogsRepository workoutLogsRepository;
    private final WorkoutLogsMapper workoutLogsMapper;

    @Override
    public WorkoutLogsResponseDTO getWorkoutLogById(String username, UUID workoutLogId) {
        WorkoutLogs workoutLogs = checkOwnership(username, workoutLogId);
        return workoutLogsMapper.toResponseDTO(workoutLogs);
    }

    @Override
    public WorkoutLogsResponseDTO createWorkoutLog(String username, WorkoutLogsRequestDTO requestDTO) {
        WorkoutLogs workoutLogs = workoutLogsMapper.toEntity(requestDTO);
        workoutLogsRepository.save(workoutLogs);
        return workoutLogsMapper.toResponseDTO(workoutLogs);
    }

    @Override
    public WorkoutLogsResponseDTO updateWorkoutLogById(String username, UUID workoutLogId, WorkoutLogsRequestDTO requestDTO) {
        WorkoutLogs workoutLogs = checkOwnership(username, workoutLogId);
        WorkoutLogs update = workoutLogsMapper.updateFromDTO(workoutLogs, requestDTO);
        WorkoutLogs savedUpdate = workoutLogsRepository.save(update);
        return workoutLogsMapper.toResponseDTO(savedUpdate);
    }

    @Override
    public void deleteWorkoutLogById(String username, UUID workoutLogId) {
        WorkoutLogs workoutLogs = checkOwnership(username, workoutLogId);
        workoutLogsRepository.deleteById(workoutLogId);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public PagedResponse<WorkoutLogsSummaryDTO> listWorkoutLogs(String username, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<WorkoutLogs> logsPage = workoutLogsRepository.findByWorkoutScheduleUserUsernameOrderByCreatedAtDesc(username, pageable);
//        Page<WorkoutLogsSummaryDTO> dtoPage = logsPage.map(workoutLogsMapper::toSummaryDTO);
//        return pagedResponse(dtoPage);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PagedResponse<WorkoutLogsSummaryDTO> listWorkoutLogsBySchedule(String username, UUID workoutScheduleId, int page, int size) {
//        if (workoutScheduleId == null) {
//            throw new IllegalArgumentException("WorkoutSchedule ID must not be null");
//        }
//        Pageable pageable = PageRequest.of(page, size);
//        Page<WorkoutLogs> logsPage = workoutLogsRepository.findByWorkoutScheduleUserUsernameAndWorkoutScheduleIdOrderByCreatedAtDesc(username, workoutScheduleId, pageable);
//        Page<WorkoutLogsSummaryDTO> dtoPage = logsPage.map(workoutLogsMapper::toSummaryDTO);
//        return pagedResponse(dtoPage);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PagedResponse<WorkoutLogsSummaryDTO> listWorkoutLogsByExercise(String username, UUID exerciseId, int page, int size) {
//        if (exerciseId == null) {
//            throw new IllegalArgumentException("Exercise ID must not be null");
//        }
//        Pageable pageable = PageRequest.of(page, size);
//        Page<WorkoutLogs> logsPage = workoutLogsRepository.findByWorkoutScheduleUserUsernameAndExerciseIdOrderByCreatedAtDesc(username, exerciseId, pageable);
//        Page<WorkoutLogsSummaryDTO> dtoPage = logsPage.map(workoutLogsMapper::toSummaryDTO);
//        return pagedResponse(dtoPage);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PagedResponse<WorkoutLogsSummaryDTO> searchWorkoutLogs(String username, String query, int page, int size) {
//        if (query == null || query.isBlank()) {
//            throw new IllegalArgumentException("Search query must not be null or blank");
//        }
//        Pageable pageable = PageRequest.of(page, size);
//        Page<WorkoutLogs> logsPage = workoutLogsRepository.searchByUserAndQuery(username, query, pageable);
//        Page<WorkoutLogsSummaryDTO> dtoPage = logsPage.map(workoutLogsMapper::toSummaryDTO);
//        return pagedResponse(dtoPage);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PagedResponse<WorkoutLogsSummaryDTO> filterWorkoutLogs(String username, WorkoutLogsFilterRequestDTO filter, int page, int size) {
//        if (filter == null) {
//            throw new IllegalArgumentException("Filter request must not be null");
//        }
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<WorkoutLogs> logsPage = workoutLogsRepository.filterWorkoutLogs(
//                username,
//                filter.getExerciseId(),
//                filter.getWorkoutScheduleId(),
//                filter.getStartDate(),
//                filter.getEndDate(),
//                filter.getMinWeight(),
//                filter.getMaxWeight(),
//                filter.getNotesQuery(),
//                pageable
//        );
//
//        Page<WorkoutLogsSummaryDTO> dtoPage = logsPage.map(workoutLogsMapper::toSummaryDTO);
//        return pagedResponse(dtoPage);
//    }



    private WorkoutLogs findWorkoutLogById(UUID id){
        return workoutLogsRepository.findById(id)
                .orElseThrow(() -> new WorkoutLogsNotFoundException("Workout log not found"));
    }

    private WorkoutLogs checkOwnership(String username, UUID workoutLogId){
        WorkoutLogs workoutLogs = findWorkoutLogById(workoutLogId);
        if (!workoutLogs.getWorkoutSchedule().getUser().getUsername().equals(username)){
            throw new CustomAccessDeniedException("Unauthorized Access");
        }
        return workoutLogs;
    }

    private PagedResponse<WorkoutLogsSummaryDTO> pagedResponse(Page<WorkoutLogsSummaryDTO> dto){
        return new PagedResponse<>(
                dto.getContent(),
                dto.getNumber(),
                dto.getSize(),
                dto.getTotalElements(),
                dto.getTotalPages(),
                dto.isLast());
    }
}
