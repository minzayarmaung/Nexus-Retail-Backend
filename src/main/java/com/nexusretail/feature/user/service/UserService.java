package com.nexusretail.feature.user.service;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.user.dto.request.UserCreateRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ApiResponse createUser(UserCreateRequest userCreateRequest);
}
