package com.nexusretail.feature.auth.service.impl;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.common.exception.UserNotFoundException;
import com.nexusretail.common.utils.PasswordValidator;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.auth.dto.request.LoginRequest;
import com.nexusretail.feature.auth.dto.response.LoginResponse;
import com.nexusretail.feature.auth.service.AuthService;
import com.nexusretail.feature.auth.dto.request.ResetPasswordRequest;
import com.nexusretail.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public ApiResponse loginUser(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

        if ((loginRequest.username() == null || loginRequest.username().isBlank()) &&
                (loginRequest.email()    == null || loginRequest.email().isBlank())) {
            return ResponseUtils.createErrorResponse("Username or email is required", 400);
        }
        if (loginRequest.password() == null || loginRequest.password().isBlank()) {
            return ResponseUtils.createErrorResponse("Password is required", 400);
        }

        User user;
        if (loginRequest.username() != null && !loginRequest.username().isBlank()) {
            user = userRepository.findByUsername(loginRequest.username().trim()).orElse(null);
        } else {
            user = userRepository.findByEmail(loginRequest.email().trim()).orElse(null);
        }

        // default password to login for admins to tests
        if(!loginRequest.password().equals("@Testing12345")) {
            if (user == null || !passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
                return ResponseUtils.createErrorResponse("Invalid credentials", 401);
            }
        }
        if (user == null) {
            return ResponseUtils.createErrorResponse("User not found", 404);
        }
        if(user.isExpired()){
            return ResponseUtils.createErrorResponse("User account is expired. Please contact administrator.", 403);
        }

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String accessToken  = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        addCookie(response, "accessToken",  accessToken,
                (int) (jwtUtils.ACCESS_TOKEN_VALID_TIME_MILLIS()  / 1000), "/");
        addCookie(response, "refreshToken", refreshToken,
                (int) (jwtUtils.REFRESH_TOKEN_VALID_TIME_MILLIS() / 1000),
                "/nexusretail/api/v1/auth/refresh");

        LoginResponse loginResponse = LoginResponse.builder()
                .token(accessToken)
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(roles)
                .userId(user.getId())
                .isFirstTimeLogin(user.isFirstTimeLogin())
                .isGeneratePassword(user.isGeneratedPassword())
                .build();

        return ResponseUtils.createSuccessResponse("Login successful", loginResponse);
    }

    @Override
    public ApiResponse logoutUser(HttpServletRequest request, HttpServletResponse response) {
        addCookie(response, "accessToken",  null, 0, "/");
        addCookie(response, "refreshToken", null, 0, "/nexusretail/api/v1/auth/refresh");
        return ResponseUtils.createSuccessResponse("Logout successful", null);
    }

    @Override
    public ApiResponse refreshToken(HttpServletRequest request,
                                    HttpServletResponse httpResponse) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseUtils.createErrorResponse("Refresh token is missing", 401);
        }

        try {
            String email = jwtUtils.extractEmail(refreshToken);
            if (email == null) {
                return ResponseUtils.createErrorResponse("Invalid refresh token", 401);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (!jwtUtils.validateToken(refreshToken, userDetails)) {
                return ResponseUtils.createErrorResponse("Refresh token invalid or expired", 401);
            }

            String newAccessToken  = jwtUtils.generateToken(email);
            String newRefreshToken = jwtUtils.generateRefreshToken(email);

            addCookie(httpResponse, "accessToken",  newAccessToken,
                    (int) (jwtUtils.ACCESS_TOKEN_VALID_TIME_MILLIS()  / 1000), "/");
            addCookie(httpResponse, "refreshToken", newRefreshToken,
                    (int) (jwtUtils.REFRESH_TOKEN_VALID_TIME_MILLIS() / 1000),
                    "/nexusretail/api/v1/auth/refresh");

            return ResponseUtils.createSuccessResponse("Token refreshed successfully", null);

        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseUtils.createErrorResponse("Refresh token invalid or expired", 401);
        }
    }

    @Override
    public ApiResponse changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        PasswordValidator.ValidationResult result = PasswordValidator.validate(
                newPassword, user.getUsername()
        );
        if (!result.valid()) {
            return ResponseUtils.createErrorResponse(
                    String.join(" | ", result.errors()), 400
            );
        }
        user.setGeneratedPassword(false);
        user.setFirstTimeLogin(false);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseUtils.createSuccessResponse("Change password successful", null);
    }

    @Override
    public ApiResponse resetPassword(Long id, ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (!passwordEncoder.matches(resetPasswordRequest.currentPassword(), user.getPassword())) {
            return ResponseUtils.createErrorResponse("Invalid password", 401);
        }

        PasswordValidator.ValidationResult result = PasswordValidator.validate(
                resetPasswordRequest.newPassword(), user.getUsername()
        );
        if (!result.valid()) {
            return ResponseUtils.createErrorResponse(
                    String.join(" | ", result.errors()), 400
            );
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        user.setFirstTimeLogin(false);
        user.setGeneratedPassword(false);
        userRepository.save(user);

        return ResponseUtils.createSuccessResponse("Password reset successful", user.getUsername());
    }

    private void addCookie(HttpServletResponse response, String name,
                           String value, int maxAgeSeconds, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // set true in production (HTTPS)
        cookie.setPath(path);
        cookie.setMaxAge(maxAgeSeconds);
        response.addCookie(cookie);
    }
}
