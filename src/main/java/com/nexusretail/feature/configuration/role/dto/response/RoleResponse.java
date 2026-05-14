package com.nexusretail.feature.configuration.role.dto.response;

import com.nexusretail.data.models.Permission;
import lombok.Builder;
import java.util.Collection;

@Builder
public record RoleResponse(
        Long id,
        String name,
        String description,
        Boolean is_disabled,
        Collection<Permission> permissionUsageData
) {}
