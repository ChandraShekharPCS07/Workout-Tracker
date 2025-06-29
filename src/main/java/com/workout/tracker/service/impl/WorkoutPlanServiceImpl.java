package com.workout.tracker.service.impl;

import com.workout.tracker.dto.*;
import com.workout.tracker.exception.CustomAccessDeniedException;
import com.workout.tracker.exception.UserNotFoundException;
import com.workout.tracker.exception.WorkoutPlanNotFoundException;
import com.workout.tracker.mappers.WorkoutPlanMapper;
import com.workout.tracker.model.User;
import com.workout.tracker.model.WorkoutPlan;
import com.workout.tracker.repository.UserRepository;
import com.workout.tracker.repository.WorkoutPlanRepository;
import com.workout.tracker.service.WorkoutPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanMapper workoutPlanMapper;
    private final UserRepository userRepository;

    @Override
    public WorkoutPlanResponseDTO getWorkoutPlanById(String username, UUID workoutPlanId) {
        WorkoutPlan workoutPlan = checkOwnership(username, workoutPlanId);
        return workoutPlanMapper.toResponse(workoutPlan);
    }

    @Override
    public WorkoutPlanResponseDTO createWorkoutPlan(String username, WorkoutPlanRequestDTO requestDTO) {
        WorkoutPlan workoutPlan = workoutPlanMapper.toEntity(requestDTO);
        workoutPlan.setUser(findUserByUsername(username));
        WorkoutPlan savedWorkoutPlan = workoutPlanRepository.save(workoutPlan);
        return workoutPlanMapper.toResponse(savedWorkoutPlan);
    }

    @Override
    public WorkoutPlanResponseDTO updateWorkoutPlan(String username, UUID workoutPlanId, WorkoutPlanRequestDTO requestDTO) {
        WorkoutPlan workoutPlan  = checkOwnership(username, workoutPlanId);
        WorkoutPlan updatedWorkoutPlan = workoutPlanMapper.updateFromDto(workoutPlan, requestDTO);
        WorkoutPlan savedWorkoutPlan = workoutPlanRepository.save(updatedWorkoutPlan);
        return workoutPlanMapper.toResponse(savedWorkoutPlan);
    }

    @Override
    public void deleteWorkoutPlan(String username, UUID workoutPlanId) {
        WorkoutPlan workoutPlan  = checkOwnership(username, workoutPlanId);
        workoutPlanRepository.deleteById(workoutPlanId);
    }

    @Override
    public PagedResponse<WorkoutPlanSummaryDTO> listUserWorkoutPlans(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<WorkoutPlan> workoutPlanPage = workoutPlanRepository.findByUserUsername(username, pageable);
        Page<WorkoutPlanSummaryDTO> dtoPage = workoutPlanPage.map(workoutPlanMapper::toSummary);
        return pagedResponse(dtoPage);
    }

    @Override
    public PagedResponse<WorkoutPlanSummaryDTO> searchUserWorkoutPlans(String username, String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<WorkoutPlan> workoutPlanPage = workoutPlanRepository.findByNameContainingIgnoreCaseAndUserUsername(query, username, pageable);
        Page<WorkoutPlanSummaryDTO> dtoPage = workoutPlanPage.map(workoutPlanMapper::toSummary);
        return pagedResponse(dtoPage);
    }

    private WorkoutPlan checkOwnership(String username, UUID workoutPlanId){
        WorkoutPlan workoutPlan = findWorkoutPlanById(workoutPlanId);
        if (!workoutPlan.getUser().getUsername().equals(username)){
            throw new CustomAccessDeniedException("You don't have permission to access this resource");
        }
        return workoutPlan;
    }

    private WorkoutPlan findWorkoutPlanById(UUID id){
        return workoutPlanRepository.findById(id)
                .orElseThrow(() -> new WorkoutPlanNotFoundException("Plan not found"));
    }

    private User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private PagedResponse<WorkoutPlanSummaryDTO> pagedResponse(Page<WorkoutPlanSummaryDTO> dto){
        return new PagedResponse<>(
                dto.getContent(),
                dto.getNumber(),
                dto.getSize(),
                dto.getTotalElements(),
                dto.getTotalPages(),
                dto.isLast());
    }
}