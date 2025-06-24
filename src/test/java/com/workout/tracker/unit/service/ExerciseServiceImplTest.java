package com.workout.tracker.unit.service;

import com.workout.tracker.dto.ExerciseRequestDTO;
import com.workout.tracker.dto.ExerciseResponseDTO;
import com.workout.tracker.dto.ExerciseSummaryDTO;
import com.workout.tracker.dto.PagedResponse;
import com.workout.tracker.mappers.ExerciseMapper;
import com.workout.tracker.model.Exercise;
import com.workout.tracker.repository.ExerciseRepository;
import com.workout.tracker.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseMapper exerciseMapper;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    private Exercise exercise;
    private ExerciseSummaryDTO summaryDTO;
    private ExerciseResponseDTO responseDTO;
    private List<Exercise> exerciseList;
    private List<ExerciseSummaryDTO> summaryDTOList;
    private List<ExerciseResponseDTO> responseDTOList;

    @BeforeEach
    void setUp(){
        exercise = new Exercise();
        exercise.setId(UUID.randomUUID());
        exercise.setName("Push-up");
        exercise.setCategory("Chest");
        exerciseList = List.of(exercise);

        summaryDTO = new ExerciseSummaryDTO();
        summaryDTO.setId(exercise.getId());
        summaryDTO.setName(exercise.getName());
        summaryDTO.setCategory(exercise.getCategory());
        summaryDTOList = List.of(summaryDTO);

        responseDTO = new ExerciseResponseDTO();
        responseDTO.setId(exercise.getId());
        responseDTO.setName(exercise.getName());
        responseDTO.setCategory(exercise.getCategory());
        responseDTOList = List.of(responseDTO);
    }

    @Test
    void testGetAllExercise_shouldReturnAllExerciseInPages_WhenPageNumbersAreCorrect(){
        int page = 0;
        int size = 1;

        Pageable pageable = PageRequest.of(page, size, Sort.by("category").ascending());
        Page<Exercise> exercisePage = new PageImpl<>(exerciseList, pageable, exerciseList.size());

        when(exerciseRepository.findAll(pageable)).thenReturn(exercisePage);
        when(exerciseMapper.toSummary(exercise)).thenReturn(summaryDTO);

        PagedResponse<ExerciseSummaryDTO> result = exerciseService.getAllExercises(page, size);

        assertNotNull(result);
    }

    @Test
    void testGetAllExercisesByCategory_shouldReturnAllExerciseByCategory_whenCategoryExercisesAvailable(){
        // arrange
        int page = 0;
        int size = 1;
        String category = "Chest";
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Exercise> exercisePage = new PageImpl<>(exerciseList, pageable, exerciseList.size());

        when(exerciseRepository.findByCategory(category, pageable)).thenReturn(exercisePage);
        when(exerciseMapper.toSummary(exercise)).thenReturn(summaryDTO);

        // act
        PagedResponse<ExerciseSummaryDTO> result = exerciseService.getAllExercisesByCategory(category, page, size);

        // assert
        assertNotNull(result);
        assertEquals("Push-up", result.getContent().getFirst().getName());
    }

    @Test
    void testGetAllExercisesByCategory_shouldReturnEmptyPage_whenCategoryExercisesNotFound(){
        // arrange
        int page = 0;
        int size = 1;
        String category = "Legs";
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Exercise> exercisePage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(exerciseRepository.findByCategory(category, pageable)).thenReturn(exercisePage);

        // act
        PagedResponse<ExerciseSummaryDTO> result = exerciseService.getAllExercisesByCategory(category, page, size);

        // assert
        assertEquals(0, result.getTotalElements());
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void testGetExerciseById_shouldReturnExercise_whenExerciseByIdFound(){
        when(exerciseRepository.findById(any(UUID.class))).thenReturn(Optional.of(exercise));
        when(exerciseMapper.toResponse(exercise)).thenReturn(responseDTO);

        ExerciseResponseDTO result = exerciseService.getExerciseById(UUID.randomUUID());

        assertNotNull(result);
        assertEquals("Push-up", result.getName());
    }

    @Test
    void testCreateExercise_shouldCreateExercise_whenNewExerciseRequestIsSent(){
        ExerciseRequestDTO requestDTO = new ExerciseRequestDTO("Push-up", "", "Chest", "");

        when(exerciseMapper.toEntity(requestDTO)).thenReturn(exercise);
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);
        when(exerciseMapper.toResponse(exercise)).thenReturn(responseDTO);

        ExerciseResponseDTO result = exerciseService.createExercise(requestDTO);

        assertNotNull(result);
        assertEquals("Chest", result.getCategory());
    }

    @Test
    void testUpdateExercise_shouldUpdateExercise_whenUpdateExercise(){
        ExerciseRequestDTO requestDTO = new ExerciseRequestDTO("Jumping Jacks", "", "Cardio", "");

        exercise.setName(requestDTO.getName());
        exercise.setCategory(requestDTO.getCategory());

        responseDTO.setName(exercise.getName());
        responseDTO.setCategory(exercise.getCategory());

        when(exerciseRepository.findById(any(UUID.class))).thenReturn(Optional.of(exercise));
        when(exerciseMapper.UpdateFromDto(exercise, requestDTO)).thenReturn(exercise);
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);
        when(exerciseMapper.toResponse(exercise)).thenReturn(responseDTO);

        ExerciseResponseDTO result = exerciseService.updateExercise(UUID.randomUUID(), requestDTO);

        assertNotNull(result);
        assertEquals("Jumping Jacks", result.getName());
    }

    @Test
    void testDeleteExercise_shouldDeleteExercise_whenDeleteById(){
        when(exerciseRepository.existsById(any(UUID.class))).thenReturn(true);
        doNothing().when(exerciseRepository).deleteById(any(UUID.class));

        exerciseService.deleteExercise(UUID.randomUUID());

        verify(exerciseRepository).deleteById(any(UUID.class));
    }

}
