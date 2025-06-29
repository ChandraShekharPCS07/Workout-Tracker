package com.workout.tracker.controller;

import com.workout.tracker.dto.*;
import com.workout.tracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    // ===== CRUD =====
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateRequestDto request) {
        UserDto created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequestDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ===== Search =====
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDto>> findUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.findUsersByRole(role));
    }

    // ===== Registration & Auth =====
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegisterDto request, HttpServletRequest httpRequest) {
        String appUrl = getAppUrl(httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request, appUrl));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/change-password/{userId}")
    public ResponseEntity<Void> changePassword(@PathVariable UUID userId, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyAccount(@RequestParam String token) {
        return ResponseEntity.ok(userService.verifyAccount(token));
    }

    // ===== Activation =====
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable UUID id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    // ===== Role Management =====
    @PostMapping("/{id}/assign-role")
    public ResponseEntity<Void> assignRole(@PathVariable UUID id, @Valid @RequestBody RoleAssignRequest request) {
        userService.assignRole(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/remove-role")
    public ResponseEntity<Void> removeRole(@PathVariable UUID id, @Valid @RequestBody RoleAssignRequest request) {
        userService.removeRole(id, request);
        return ResponseEntity.noContent().build();
    }

    // ===== Emails =====
    @PostMapping("/{id}/send-verification-email")
    public ResponseEntity<Void> sendVerificationEmail(@PathVariable UUID id, HttpServletRequest httpRequest) {
        String appUrl = getAppUrl(httpRequest);
        userService.sendVerificationEmail(id, appUrl);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-password-reset-email")
    public ResponseEntity<Void> sendPasswordResetEmail(@RequestParam String email, HttpServletRequest httpRequest) {
        String appUrl = getAppUrl(httpRequest);
        userService.sendPasswordResetEmail(email, appUrl);
        return ResponseEntity.noContent().build();
    }

    // ===== Helper =====
    private String getAppUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() +
                (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "");
    }
}

