package com.nexusretail.feature.user.service;

import com.nexusretail.common.dto.response.PaginatedApiResponse;
import com.nexusretail.feature.user.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    PaginatedApiResponse<UserResponse> getAllUsers(Pageable pageable);
}
