package com.workout.tracker.service.impl;

import com.workout.tracker.dto.WorkoutPlanRequestDTO;
import com.workout.tracker.dto.WorkoutPlanResponseDTO;
import com.workout.tracker.dto.WorkoutPlanSummaryDTO;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanMapper workoutPlanMapper;
    private final UserRepository userRepository;

    @Override
    public List<WorkoutPlanSummaryDTO> getAllWorkoutPlans(String username) {
        return workoutPlanRepository.findAllByUserUsername(username).stream().map(workoutPlanMapper::toSummary).collect(Collectors.toList());
    }

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

    private WorkoutPlan checkOwnership(String username, UUID workoutPlanId){
        WorkoutPlan workoutPlan = findWorkoutPlanById(workoutPlanId);
        if (!workoutPlan.getUser().getUsername().equals(username)){
            throw new CustomAccessDeniedException("You don't have permission to access this resource");
        }
        return workoutPlan;
    }

    private WorkoutPlan findWorkoutPlanById(UUID id){
        return workoutPlanRepository.findByIdWithExercises(id)
                .orElseThrow(() -> new WorkoutPlanNotFoundException("Plan not found"));
    }

    private User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}