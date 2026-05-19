package com.nexusretail.feature.user.service.impl;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.user.dto.request.UserCreateRequest;
import com.nexusretail.feature.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public ApiResponse createUser(UserCreateRequest userCreateRequest) {
        return null;
    }
}
