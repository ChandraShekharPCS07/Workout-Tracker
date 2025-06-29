package com.workout.tracker.service;

import com.workout.tracker.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(UserCreateRequestDto request);
    UserDto getUserById(UUID id);
    List<UserDto> getAllUsers();
    UserDto updateUser(UUID id, UserUpdateRequestDto request);
    void deleteUser(UUID id);

    List<UserDto> searchUsers(String keyword);
    List<UserDto> findUsersByRole(String role);
    Page<UserDto> getUsers(Pageable pageable);

    UserDto register(UserRegisterDto registerDto, String url);
    AuthResponse login(LoginRequestDto loginRequest);
    void changePassword(UUID userId, ChangePasswordRequest request);
    boolean isEmailTaken(String email);
    boolean verifyAccount(String token);

    void deactivateUser(UUID userId);
    void activateUser(UUID userId);
    void assignRole(UUID userId, RoleAssignRequest request);
    void removeRole(UUID userId, RoleAssignRequest request);

    void sendVerificationEmail(UUID userId, String url);
    void sendPasswordResetEmail(String email, String url);
}

