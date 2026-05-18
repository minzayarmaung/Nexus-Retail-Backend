package com.nexusretail.feature.configuration.role.dto.response;

import java.util.Map;

public record RolePermissionUpdateResponse(
        Long roleId,
        String roleName,
        Changes changes
) {
    public record Changes(Map<String, Boolean> permissions) {}
}