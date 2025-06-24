package com.workout.tracker.service.impl;

import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.dto.WorkoutLogsRequestDTO;
import com.workout.tracker.dto.WorkoutLogsResponseDTO;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutLogsServiceImpl implements WorkoutLogsService {

    private final WorkoutLogsRepository workoutLogsRepository;
    private final WorkoutLogsMapper workoutLogsMapper;

    @Override
    public PagedResponse<WorkoutLogsResponseDTO> getAllWorkoutLogs(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("workoutSchedule").ascending());
        Page<WorkoutLogs> workoutLogsPage = workoutLogsRepository.findAllByWorkoutScheduleUserUsername(username, pageable);
        Page<WorkoutLogsResponseDTO> dtoPage = workoutLogsPage.map(workoutLogsMapper::toResponseDTO);
        return pagedResponse(dtoPage);
    }

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

    private PagedResponse<WorkoutLogsResponseDTO> pagedResponse(Page<WorkoutLogsResponseDTO> dto){
        return new PagedResponse<>(
                dto.getContent(),
                dto.getNumber(),
                dto.getSize(),
                dto.getTotalElements(),
                dto.getTotalPages(),
                dto.isLast());
    }
}
