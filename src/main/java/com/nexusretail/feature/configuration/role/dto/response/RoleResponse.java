package com.nexusretail.feature.configuration.role.dto.response;

import lombok.Builder;

@Builder
public record RoleResponse(
        Long id,
        String name,
        String description,
        Boolean is_disabled
) {}
