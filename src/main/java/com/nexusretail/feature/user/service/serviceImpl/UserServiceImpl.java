package com.nexusretail.feature.user.service.serviceImpl;

import com.nexusretail.common.dto.response.PaginatedApiResponse;
import com.nexusretail.common.dto.response.PaginationMeta;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.user.dto.response.UserResponse;
import com.nexusretail.feature.user.mapper.UserMapper;
import com.nexusretail.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public PaginatedApiResponse<UserResponse> getAllUsers(Pageable pageable) {

        Page<User> userPage = userRepository.findAllWithShopAndRole(pageable);

        List<UserResponse> data = userPage.getContent()
                .stream()
                .map(userMapper::toResponse)
                .toList();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(userPage.getNumber() + 1)
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .pageSize(userPage.getSize())
                .build();

        return PaginatedApiResponse.<UserResponse>builder()
                .data(data)
                .meta(meta)
                .build();
    }
}
