package com.workout.tracker.unit.service;

import com.workout.tracker.dto.WorkoutPlanExerciseRequestDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseResponseDTO;
import com.workout.tracker.dto.WorkoutPlanExerciseSummaryDTO;
import com.workout.tracker.mappers.WorkoutPlanExerciseMapper;
import com.workout.tracker.model.*;
import com.workout.tracker.repository.WorkoutPlanExerciseRepository;
import com.workout.tracker.repository.WorkoutPlanRepository;
import com.workout.tracker.service.impl.WorkoutPlanExerciseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutPlanExerciseServiceImplTest {

    @Mock
    private WorkoutPlanExerciseRepository workoutPlanExerciseRepository;

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private WorkoutPlanExerciseMapper workoutPlanExerciseMapper;

    @InjectMocks
    private WorkoutPlanExerciseServiceImpl workoutPlanExerciseService;

    private User user;
    private WorkoutPlanExercise workoutPlanExercise1;
    private WorkoutPlanExercise workoutPlanExercise2;
    private List<WorkoutPlanExercise> workoutPlanExerciseList;
    private WorkoutPlan workoutPlan;
    private WorkoutPlanExerciseSummaryDTO summaryDTO1;
    private WorkoutPlanExerciseResponseDTO responseDTO1;
    private WorkoutPlanExerciseSummaryDTO summaryDTO2;
    private WorkoutPlanExerciseResponseDTO responseDTO2;

    @BeforeEach
    void setup(){
        workoutPlanExerciseList = new ArrayList<>();

        user = new User();
        user.setUsername("chandra");
        user.setRole(Role.USER);

        workoutPlan = new WorkoutPlan();
        workoutPlan.setId(UUID.randomUUID());
        workoutPlan.setName("Chest Workouts");
        workoutPlan.setUser(user);

        Exercise pushUP = new Exercise();
        pushUP.setName("Push-up");
        Exercise pullUp = new Exercise();
        pullUp.setName("Pull-up");

        workoutPlanExercise1 = new WorkoutPlanExercise();
        workoutPlanExercise1.setId(UUID.randomUUID());
        workoutPlanExercise1.setWorkoutPlan(workoutPlan);
        workoutPlanExercise1.setExercise(pushUP);
        workoutPlanExerciseList.add(workoutPlanExercise1);

        workoutPlanExercise2 = new WorkoutPlanExercise();
        workoutPlanExercise2.setId(UUID.randomUUID());
        workoutPlanExercise2.setWorkoutPlan(workoutPlan);
        workoutPlanExercise2.setExercise(pullUp);
        workoutPlanExerciseList.add(workoutPlanExercise2);

        workoutPlan.setExerciseList(workoutPlanExerciseList);

        summaryDTO1 = new WorkoutPlanExerciseSummaryDTO();
        summaryDTO1.setId(workoutPlanExercise1.getId());
        summaryDTO1.setExerciseName(workoutPlanExercise1.getExercise().getName());
        responseDTO1 = new WorkoutPlanExerciseResponseDTO();
        responseDTO1.setId(workoutPlanExercise1.getId());
        responseDTO1.setExerciseName(workoutPlanExercise1.getExercise().getName());

        summaryDTO2 = new WorkoutPlanExerciseSummaryDTO();
        summaryDTO2.setId(workoutPlanExercise2.getId());
        summaryDTO2.setExerciseName(workoutPlanExercise2.getExercise().getName());
        responseDTO2 = new WorkoutPlanExerciseResponseDTO();
        responseDTO2.setId(workoutPlanExercise2.getId());
        responseDTO2.setExerciseName(workoutPlanExercise2.getExercise().getName());
    }

    @Test
    void testGetAllWorkoutPlanExerciseByWorkoutPlanId_shouldReturnAllWorkoutPlanById_whenWorkoutPlanByIdFound(){
        when(workoutPlanRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanExerciseRepository.getAllByWorkoutPlanId(any(UUID.class))).thenReturn(List.of(workoutPlanExercise1, workoutPlanExercise2));
        when(workoutPlanExerciseMapper.toSummaryDTO(workoutPlanExercise1)).thenReturn(summaryDTO1);
        when(workoutPlanExerciseMapper.toSummaryDTO(workoutPlanExercise2)).thenReturn(summaryDTO2);

        List<WorkoutPlanExerciseSummaryDTO> result =
                workoutPlanExerciseService.getAllWorkoutPlanExerciseByWorkoutPlanId("chandra", workoutPlan.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetWorkoutPlanExerciseById_shouldReturnWorkoutPlanExercises_whenWorkoutPlanExercisesFound(){
        when(workoutPlanRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanExerciseRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlanExercise1));
        when(workoutPlanExerciseMapper.toResponseDTO(workoutPlanExercise1)).thenReturn(responseDTO1);

        WorkoutPlanExerciseResponseDTO result =
                workoutPlanExerciseService.getWorkoutPlanExerciseById("chandra", workoutPlanExercise1.getId(), workoutPlan.getId());

        assertNotNull(result);
        assertEquals("Push-up", result.getExerciseName());
    }

    @Test
    void testCreateWorkoutPlanExercise_shouldCreateExerciseInWorkoutPlan_whenWorkoutPlanExerciseIsFound(){
        WorkoutPlanExerciseRequestDTO requestDTO = new WorkoutPlanExerciseRequestDTO();
        requestDTO.setExerciseName("Jumping Jacks");

        Exercise jumpingJacks = new Exercise();
        jumpingJacks.setName(requestDTO.getExerciseName());

        WorkoutPlanExercise workoutPlanExercise3 = new WorkoutPlanExercise();
        workoutPlanExercise3.setId(UUID.randomUUID());
        workoutPlanExercise3.setWorkoutPlan(workoutPlan);
        workoutPlanExercise3.setExercise(jumpingJacks);
        workoutPlanExerciseList.add(workoutPlanExercise3);

        workoutPlan.setExerciseList(workoutPlanExerciseList);

        WorkoutPlanExerciseResponseDTO responseDTO3 = new WorkoutPlanExerciseResponseDTO();
        responseDTO3.setId(workoutPlanExercise3.getId());
        responseDTO3.setExerciseName(jumpingJacks.getName());

        when(workoutPlanRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanExerciseMapper.toEntity(requestDTO)).thenReturn(workoutPlanExercise3);
        when(workoutPlanExerciseRepository.save(any(WorkoutPlanExercise.class))).thenReturn(workoutPlanExercise3);
        when(workoutPlanExerciseMapper.toResponseDTO(workoutPlanExercise3)).thenReturn(responseDTO3);

        WorkoutPlanExerciseResponseDTO result = workoutPlanExerciseService.createWorkoutPlanExercise(user.getUsername(), requestDTO, workoutPlan.getId());

        assertNotNull(result);
        assertEquals("Jumping Jacks", result.getExerciseName());
    }

    @Test
    void testUpdateWorkoutPlanExercise_shouldUpdateWorkoutPlanExercise_whenWorkoutPlanExerciseFound(){
        WorkoutPlanExerciseRequestDTO requestDTO = new WorkoutPlanExerciseRequestDTO();
        requestDTO.setExerciseName("Jumping Jacks");

        Exercise jumpingJacks = new Exercise();
        jumpingJacks.setId(UUID.randomUUID());
        jumpingJacks.setName(requestDTO.getExerciseName());

        workoutPlanExercise1.setExercise(jumpingJacks);

        responseDTO1.setExerciseName(workoutPlanExercise1.getExercise().getName());

        when(workoutPlanRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanExerciseRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlanExercise1));
        when(workoutPlanExerciseMapper.updateFromDTO(workoutPlanExercise1, requestDTO)).thenReturn(workoutPlanExercise1);
        when(workoutPlanExerciseRepository.save(workoutPlanExercise1)).thenReturn(workoutPlanExercise1);
        when(workoutPlanExerciseMapper.toResponseDTO(workoutPlanExercise1)).thenReturn(responseDTO1);

        WorkoutPlanExerciseResponseDTO result = workoutPlanExerciseService.updateWorkoutPlanExercise(user.getUsername(), workoutPlanExercise1.getId(), requestDTO, workoutPlan.getId());

        assertNotNull(result);
        assertEquals("Jumping Jacks", result.getExerciseName());
    }

    @Test
    void testDeleteWorkoutPlanExercise_ShouldDeleteExercise_whenExerciseWithWorkoutPlanFound(){
        when(workoutPlanRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanExerciseRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlanExercise1));
        doNothing().when(workoutPlanExerciseRepository).deleteById(any(UUID.class));

        workoutPlanExerciseService.deleteWorkoutPlanExercise(user.getUsername(), workoutPlanExercise1.getId(), workoutPlan.getId());

        verify(workoutPlanExerciseRepository).deleteById(workoutPlanExercise1.getId());
    }

}