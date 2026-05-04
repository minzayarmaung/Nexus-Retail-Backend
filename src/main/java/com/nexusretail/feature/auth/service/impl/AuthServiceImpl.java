package com.nexusretail.feature.auth.service.impl;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.auth.dto.request.LoginRequest;
import com.nexusretail.feature.auth.dto.response.LoginResponse;
import com.nexusretail.feature.auth.service.AuthService;
import com.nexusretail.security.Jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public ApiResponse loginUser(LoginRequest loginRequest, HttpServletRequest request) {
        try {
            // Validate input
            if ((loginRequest.username() == null || loginRequest.username().trim().isEmpty()) &&
                (loginRequest.email() == null || loginRequest.email().trim().isEmpty())) {
                return ResponseUtils.createErrorResponse("Username or email is required", 400);
            }

            if (loginRequest.password() == null || loginRequest.password().trim().isEmpty()) {
                return ResponseUtils.createErrorResponse("Password is required", 400);
            }

            User user;

            // Try to find user by username (case sensitive) first, then by email
            if (loginRequest.username() != null && !loginRequest.username().trim().isEmpty()) {
                user = userRepository.findByUsername(loginRequest.username().trim())
                        .orElse(null);
            } else {
                user = userRepository.findByEmail(loginRequest.email().trim())
                        .orElse(null);
            }

            if (user == null) {
                return ResponseUtils.createErrorResponse("Invalid credentials", 401);
            }

            // Verify password
            if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
                return ResponseUtils.createErrorResponse("Invalid credentials", 401);
            }

            // Generate JWT token
            String token = jwtUtils.generateToken(user);

            // Create response
            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().getName())
                    .userId(user.getId())
                    .build();

            return ResponseUtils.createSuccessResponse("Login successful", loginResponse);

        } catch (Exception e) {
            return ResponseUtils.createErrorResponse("Login failed: " + e.getMessage(), 500);
        }
    }
}
