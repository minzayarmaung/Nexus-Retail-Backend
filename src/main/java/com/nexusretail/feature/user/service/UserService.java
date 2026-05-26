package com.nexusretail.feature.user.service;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.user.dto.request.UserCreateRequest;
import com.nexusretail.feature.user.dto.request.UserUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ApiResponse createUser(UserCreateRequest userCreateRequest);

    Boolean checkUsername(String username);

    String generatePassword(String username);

    String suspendUser(Long id);

    ApiResponse updateUser(UserUpdateRequest userUpdateRequest, Long id);

    ApiResponse getUsers();
}
