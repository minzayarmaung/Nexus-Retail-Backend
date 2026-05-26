package com.nexusretail.system.configuration.role.dto.request;
import java.util.Map;

public record UpdateRolePermissionsRequest(
        Map<String, Boolean> permissions
) {}