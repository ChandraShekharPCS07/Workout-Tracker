package com.workout.tracker.repository;

import com.workout.tracker.model.WorkoutLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface WorkoutLogsRepository extends JpaRepository<WorkoutLogs, UUID> {

//    /**
//     * List all logs for a user with paging.
//     */
//    Page<WorkoutLogs> findByWorkoutScheduleUserUsernameOrderByCreatedAtDesc(
//            String username,
//            Pageable pageable
//    );
//
//    /**
//     * List logs by workout schedule for user.
//     */
//    Page<WorkoutLogs> findByWorkoutScheduleUserUsernameAndWorkoutScheduleIdOrderByCreatedAtDesc(
//            String username,
//            UUID workoutScheduleId,
//            Pageable pageable
//    );
//
//    /**
//     * List logs by exercise for user.
//     */
//    Page<WorkoutLogs> findByWorkoutScheduleUserUsernameAndExerciseIdOrderByCreatedAtDesc(
//            String username,
//            UUID exerciseId,
//            Pageable pageable
//    );
//
//    /**
//     * Free-text search in notes or exercise name.
//     */
//    @Query("""
//    SELECT wl FROM WorkoutLogs wl
//    WHERE wl.workoutSchedule.user.username = :username
//      AND (
//          LOWER(wl.notes) LIKE LOWER(CONCAT('%', :query, '%'))
//          OR LOWER(wl.exercise.name) LIKE LOWER(CONCAT('%', :query, '%'))
//      )
//""")
//    Page<WorkoutLogs> searchByUserAndQuery(
//            @Param("username") String username,
//            @Param("query") String query,
//            Pageable pageable
//    );
//
//    /**
//     * Advanced filter with multiple optional parameters.
//     */
//    @Query("""
//    SELECT wl FROM WorkoutLogs wl
//    WHERE wl.workoutSchedule.user.username = :username
//      AND (:exerciseId IS NULL OR wl.exercise.id = :exerciseId)
//      AND (:scheduleId IS NULL OR wl.workoutSchedule.id = :scheduleId)
//      AND (:startDate IS NULL OR wl.createdAt >= :startDate)
//      AND (:endDate IS NULL OR wl.createdAt <= :endDate)
//      AND (:minWeight IS NULL OR wl.actualWeightKg >= :minWeight)
//      AND (:maxWeight IS NULL OR wl.actualWeightKg <= :maxWeight)
//      AND (
//          :notesQuery IS NULL OR
//          LOWER(wl.notes) LIKE LOWER(CONCAT('%', :notesQuery, '%'))
//      )
//""")
//    Page<WorkoutLogs> filterWorkoutLogs(
//            @Param("username") String username,
//            @Param("exerciseId") UUID exerciseId,
//            @Param("scheduleId") UUID scheduleId,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("minWeight") Float minWeight,
//            @Param("maxWeight") Float maxWeight,
//            @Param("notesQuery") String notesQuery,
//            Pageable pageable
//    );

}

