package com.workout.tracker.repository;

import com.workout.tracker.model.Role;
import com.workout.tracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity.
 *
 * Provides CRUD operations, custom search, and role-based filtering.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find a user by their unique username.
     *
     * @param username the username to search for
     * @return Optional containing the User if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by their unique email.
     *
     * @param email the email to search for
     * @return Optional containing the User if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists with the given email.
     *
     * @param email the email to check
     * @return true if a user with the email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find all users with the specified role.
     *
     * @param role the role to filter by
     * @return list of users with the given role
     */
    List<User> findByRole(Role role);

    /**
     * Returns a paginated list of all users.
     *
     * @param pageable pagination information
     * @return page of users
     */
    @Override
    Page<User> findAll(Pageable pageable);

    /**
     * Search users by keyword in username or email (case-insensitive).
     *
     * @param keyword the search keyword
     * @return list of users matching the search
     */
    @Query("""
        SELECT u FROM User u
         WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    List<User> searchUsersByKeyword(@Param("keyword") String keyword);

}

