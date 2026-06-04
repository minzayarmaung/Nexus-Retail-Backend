package com.nexusretail.features.configuration.role.dto.request;

import lombok.Builder;

@Builder
public record RoleRequest(

        String name,

        String description,

        boolean is_disabled
){}
