package com.nexusretail.feature.auth.service.impl;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.auth.dto.request.LoginRequest;
import com.nexusretail.feature.auth.dto.response.LoginResponse;
import com.nexusretail.feature.auth.service.AuthService;
import com.nexusretail.security.Jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ApiResponse loginUser(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
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

            ApiResponse apiResponse = ResponseUtils.createSuccessResponse("Login successful", loginResponse);

            // Set JWT token in cookie for successful login
            setJwtCookie(response, token);

            return apiResponse;

        } catch (Exception e) {
            return ResponseUtils.createErrorResponse("Login failed: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse logoutUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Clear JWT token cookie
            clearJwtCookie(response);
            return ResponseUtils.createSuccessResponse("Logout successful", null);
        } catch (Exception e) {
            return ResponseUtils.createErrorResponse("Logout failed: " + e.getMessage(), 500);
        }
    }

    /**
     * Set JWT token in HttpOnly cookie
     */
    private void setJwtCookie(HttpServletResponse response, String token) {
        try {
            if (token != null && !token.isEmpty()) {
                // Create secure cookie for JWT token
                Cookie jwtCookie = new Cookie("jwt_token", token);
                jwtCookie.setHttpOnly(true); // Prevent JavaScript access
                jwtCookie.setSecure(false); // Set to true in production with HTTPS
                jwtCookie.setPath("/"); // Available for all paths
                jwtCookie.setMaxAge(3600); // 1 hour (matches JWT expiration)

                // Add cookie to response
                response.addCookie(jwtCookie);
            }
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Error setting JWT cookie: " + e.getMessage());
        }
    }

    /**
     * Clear JWT token cookie by setting maxAge to 0
     */
    private void clearJwtCookie(HttpServletResponse response) {
        try {
            Cookie jwtCookie = new Cookie("jwt_token", null);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // Set to true in production with HTTPS
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(0); // Expire immediately

            response.addCookie(jwtCookie);
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Error clearing JWT cookie: " + e.getMessage());
        }
    }
}
