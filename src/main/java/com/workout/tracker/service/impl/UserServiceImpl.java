package com.workout.tracker.service.impl;

import com.workout.tracker.dto.*;
import com.workout.tracker.exception.UnauthorizedException;
import com.workout.tracker.exception.UserNotFoundException;
import com.workout.tracker.mappers.UserMapper;
import com.workout.tracker.model.PasswordResetToken;
import com.workout.tracker.model.Role;
import com.workout.tracker.model.User;
import com.workout.tracker.model.VerificationToken;
import com.workout.tracker.repository.PasswordResetTokenRepository;
import com.workout.tracker.repository.UserRepository;
import com.workout.tracker.repository.VerificationTokenRepository;
import com.workout.tracker.security.CustomUserDetailsService;
import com.workout.tracker.security.JwtService;
import com.workout.tracker.service.EmailService;
import com.workout.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;


    @Override
    public UserDto createUser(UserCreateRequestDto request) {
        // Map DTO to Entity
        User user = userMapper.fromCreateRequest(request);

        // Encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Default fields if not set
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        // Save and return DTO
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Override
    public UserDto updateUser(UUID id, UserUpdateRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        // Partial update with MapStruct
        userMapper.updateUserFromDto(request, user);

        // Save and return
        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        userRepository.delete(user); // triggers @SQLDelete to soft-delete
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> searchUsers(String keyword) {
        List<User> users = userRepository.searchUsersByKeyword(keyword);
        return userMapper.toDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findUsersByRole(String role) {
        Role parsedRole;
        try {
            parsedRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        List<User> users = userRepository.findByRole(parsedRole);
        return userMapper.toDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(userMapper::toDto);
    }


    @Override
    public UserDto register(UserRegisterDto registerDto, String url) {
        if (isEmailTaken(registerDto.getEmail())) {
            throw new IllegalArgumentException("Email already taken.");
        }
        User user = userMapper.fromRegisterRequest(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEnabled(false);
        user.setRole(Role.USER);
        User saved = userRepository.save(user);
        sendVerificationEmail(saved.getId(), url);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password."));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password.");
        }
        if (!user.isEnabled()) {
            throw new UnauthorizedException("Account is not verified or activated.");
        }
        String accessToken = jwtService.generateToken(customUserDetailsService.loadUserByUsername(user.getUsername()));
//        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .token(accessToken)
//                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token."));
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return true;
    }


    @Override
    public void deactivateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void assignRole(UUID userId, RoleAssignRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Role role;
        try {
            role = Role.valueOf(request.getRole().name().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + request.getRole());
        }

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void removeRole(UUID userId, RoleAssignRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Role role;
        try {
            role = Role.valueOf(request.getRole().name().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + request.getRole());
        }

        if (!user.getRole().equals(role)) {
            throw new IllegalArgumentException("User does not have the specified role: " + role);
        }

        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Override
    public void sendVerificationEmail(UUID userId, String url) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = verificationTokenRepository
                .findByUser(user)
                .orElse(new VerificationToken());

        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now().plus(24, ChronoUnit.HOURS));

        verificationTokenRepository.save(verificationToken);

        String verificationLink = url + "/verify?token=" + token;

        String content = String.format(
                "Hi %s,\n\nPlease verify your account using the following link:\n%s\n\nThis link will expire in 24 hours.",
                user.getUsername(),
                verificationLink
        );

        emailService.sendEmail(user.getEmail(), "Verify Your Account", content);
    }

    @Override
    public void sendPasswordResetEmail(String email, String url) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email not found."));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByUser(user)
                .orElse(new PasswordResetToken());

        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS));

        passwordResetTokenRepository.save(resetToken);

        String resetLink = url + "/reset-password?token=" + token;

        String content = String.format(
                "Hi %s,\n\nYou can reset your password using the following link:\n%s\n\nThis link will expire in 1 hour.",
                user.getUsername(),
                resetLink
        );

        emailService.sendEmail(user.getEmail(), "Reset Your Password", content);
    }







}

