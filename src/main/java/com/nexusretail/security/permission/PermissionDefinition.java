package com.nexusretail.security.permission;

import lombok.Builder;

@Builder
public record PermissionDefinition(
        String code,
        String grouping,
        String entityName,
        String actionName
){}
