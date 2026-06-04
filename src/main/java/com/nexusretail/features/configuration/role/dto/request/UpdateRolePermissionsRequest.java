package com.nexusretail.features.configuration.role.dto.request;
import java.util.Map;

public record UpdateRolePermissionsRequest(
        Map<String, Boolean> permissions
) {}