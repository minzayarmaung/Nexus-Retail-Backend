package com.nexusretail.feature.user.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserListResponse(
        long id,
        String username,
        String firstName,
        String lastName,
        String email,
        boolean generatePassword,
        boolean cannotChangePassword,
        List<String> Roles
) {}
