package com.nexusretail.feature.user.dto.request;

import lombok.Builder;

@Builder
public record UserCreateRequest(
        String username,
        String email,
        String firstName,
        String lastName,
        String password,
        boolean generatePassword,
        boolean cannotChangePassword
) {}
