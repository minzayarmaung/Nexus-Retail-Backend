package com.nexusretail.feature.auth.service;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.auth.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ApiResponse loginUser(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
    ApiResponse logoutUser(HttpServletRequest request, HttpServletResponse response);
}
