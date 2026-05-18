package com.nexusretail.feature.configuration.role.dto.response;

import com.nexusretail.feature.configuration.permission.dto.response.PermissionResponse;
import lombok.Builder;
import java.util.Collection;

@Builder
public record RolePermissionResponse(
        Long id,
        String name,
        String description,
        Boolean is_disabled,
        Collection<PermissionResponse> permissionUsageData
) {}
