package com.nexusretail.system.auth.service;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.system.auth.dto.request.LoginRequest;
import com.nexusretail.system.auth.dto.request.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ApiResponse loginUser(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
    ApiResponse logoutUser(HttpServletRequest request, HttpServletResponse response);

    ApiResponse refreshToken(HttpServletRequest request, HttpServletResponse httpResponse);

    ApiResponse changePassword(Long id, String newPassword);

    ApiResponse resetPassword(Long id, ResetPasswordRequest resetPasswordRequest);
}
