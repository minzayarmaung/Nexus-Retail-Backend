package com.nexusretail.feature.configuration.permission.dto.response;

import lombok.Builder;

@Builder
public record PermissionResponse(
        String grouping,
        String code,
        String entityName,
        String actionName,
        Boolean selected
) {}
