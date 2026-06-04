package com.nexusretail.features.configuration.role.dto.response;

import com.nexusretail.features.configuration.permission.dto.response.PermissionResponse;
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
