package com.workout.tracker.unit.service;

import com.workout.tracker.dto.WorkoutPlanRequestDTO;
import com.workout.tracker.dto.WorkoutPlanResponseDTO;
import com.workout.tracker.dto.WorkoutPlanSummaryDTO;
import com.workout.tracker.mappers.WorkoutPlanMapper;
import com.workout.tracker.mappers.WorkoutPlanMapperImpl;
import com.workout.tracker.model.Role;
import com.workout.tracker.model.User;
import com.workout.tracker.model.WorkoutPlan;
import com.workout.tracker.repository.UserRepository;
import com.workout.tracker.repository.WorkoutPlanRepository;
import com.workout.tracker.service.impl.WorkoutPlanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutPlanServiceImplTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkoutPlanMapper workoutPlanMapper = new WorkoutPlanMapperImpl();

    @InjectMocks
    private WorkoutPlanServiceImpl workoutPlanService;

    private User user;
    private WorkoutPlan workoutPlan;
    private WorkoutPlanResponseDTO responseDTO;
    private WorkoutPlanSummaryDTO summaryDTO;
    private List<WorkoutPlan> workoutPlanList;
    private List<WorkoutPlanSummaryDTO> summaryDTOList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("chandra");
        user.setRole(Role.ADMIN);

        workoutPlan = new WorkoutPlan();
        workoutPlan.setId(UUID.randomUUID());
        workoutPlan.setUser(user);
        workoutPlan.setName("Push-Up");

        responseDTO = new WorkoutPlanResponseDTO();
        responseDTO.setId(workoutPlan.getId());
        responseDTO.setName(workoutPlan.getName());

        summaryDTO = new WorkoutPlanSummaryDTO();
        summaryDTO.setId(workoutPlan.getId());
        summaryDTO.setName(workoutPlan.getName());

        workoutPlanList = List.of(workoutPlan);

        summaryDTOList = List.of(summaryDTO);

    }

    @Test
    void testGetAllWorkoutPlan_shouldReturnAllExerciseList_whenAvailable(){
        when(workoutPlanRepository.findAllByUserUsername("chandra")).thenReturn(workoutPlanList);
        when(workoutPlanMapper.toSummary(workoutPlan)).thenReturn(summaryDTO);

        List<WorkoutPlanSummaryDTO> result = workoutPlanService.getAllWorkoutPlans("chandra");

        assertNotNull(result);
        assertEquals("Push-Up", result.getFirst().getName());
    }

    @Test
    void testGetWorkoutPlanById_shouldReturnWorkoutPlanById_whenWorkoutPlanExists(){
        when(workoutPlanRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanMapper.toResponse(workoutPlan)).thenReturn(responseDTO);

        WorkoutPlanResponseDTO result = workoutPlanService.getWorkoutPlanById(user.getUsername(), workoutPlan.getId());

        assertNotNull(result);
        assertEquals("Push-Up", result.getName());
    }

    @Test
    void testCreateWorkoutPlan_shouldCreateNewWorkoutPlan_whenWorkoutCreated(){
        WorkoutPlanRequestDTO requestDTO = new WorkoutPlanRequestDTO("Squats", "Leg Exercise");

        WorkoutPlan newWorkoutPlan = new WorkoutPlan();
        newWorkoutPlan.setId(UUID.randomUUID());
        newWorkoutPlan.setName(requestDTO.getName());
        newWorkoutPlan.setUser(user);
        newWorkoutPlan.setNotes(requestDTO.getNotes());

        WorkoutPlanResponseDTO newResponseDto = new WorkoutPlanResponseDTO();
        newResponseDto.setName(newWorkoutPlan.getName());
        newResponseDto.setId(newWorkoutPlan.getId());
        newResponseDto.setNotes(newWorkoutPlan.getNotes());

        when(workoutPlanMapper.toEntity(requestDTO)).thenReturn(newWorkoutPlan);
        when(userRepository.findByUsername("chandra")).thenReturn(Optional.of(user));
        when(workoutPlanRepository.save(newWorkoutPlan)).thenReturn(newWorkoutPlan);
        when(workoutPlanMapper.toResponse(newWorkoutPlan)).thenReturn(newResponseDto);

        WorkoutPlanResponseDTO result = workoutPlanService.createWorkoutPlan(user.getUsername(), requestDTO);


        assertNotNull(result);
        assertEquals("Squats", result.getName());
    }

    @Test
    void testUpdateWorkoutPlan_shouldUpdatePlan_whenWorkoutPlanExist(){
        WorkoutPlanRequestDTO requestDTO = new WorkoutPlanRequestDTO("Squats", "Leg Exercise");

        workoutPlan.setName(requestDTO.getName());
        workoutPlan.setNotes(requestDTO.getNotes());

        responseDTO.setName(workoutPlan.getName());
        responseDTO.setNotes(workoutPlan.getNotes());

        when(workoutPlanRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanMapper.updateFromDto(workoutPlan, requestDTO)).thenReturn(workoutPlan);
        when(workoutPlanRepository.save(workoutPlan)).thenReturn(workoutPlan);
        when(workoutPlanMapper.toResponse(workoutPlan)).thenReturn(responseDTO);


        WorkoutPlanResponseDTO result = workoutPlanService.updateWorkoutPlan(user.getUsername(), workoutPlan.getId(), requestDTO);

        assertNotNull(result);
        assertEquals("Squats", result.getName());
    }

    @Test
    void testDeleteWorkoutPlan_shouldDeleteWorkoutPlan_WhenAuthorizedUserDeletes(){
        when(workoutPlanRepository.findById(any(UUID.class))).thenReturn(Optional.of(workoutPlan));
        doNothing().when(workoutPlanRepository).deleteById(any(UUID.class));

        workoutPlanService.deleteWorkoutPlan(user.getUsername(), workoutPlan.getId());

        verify(workoutPlanRepository).findById(any(UUID.class));
        verify(workoutPlanRepository).deleteById(any(UUID.class));
    }
}

