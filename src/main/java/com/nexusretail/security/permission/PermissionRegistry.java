package com.nexusretail.security.permission;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import static com.nexusretail.security.permission.PermissionCode.*;

@RequiredArgsConstructor
public final class PermissionRegistry {

    public static List<PermissionDefinition> all() {
        return List.of(
                // Special
                new PermissionDefinition(ALL_FUNCTIONS,      "special", null,   null),
                new PermissionDefinition(ALL_FUNCTIONS_READ, "special", null,   null),

                // Configuration – Codes
                new PermissionDefinition(READ_CODE,   "configuration", "CODE", "READ"),
                new PermissionDefinition(CREATE_CODE, "configuration", "CODE", "CREATE"),
                new PermissionDefinition(UPDATE_CODE, "configuration", "CODE", "UPDATE"),
                new PermissionDefinition(DELETE_CODE, "configuration", "CODE", "DELETE"),

                // Configuration – Roles
                new PermissionDefinition(READ_ROLE,   "configuration", "ROLE", "READ"),
                new PermissionDefinition(CREATE_ROLE, "configuration", "ROLE", "CREATE"),
                new PermissionDefinition(UPDATE_ROLE, "configuration", "ROLE", "UPDATE"),
                new PermissionDefinition(DELETE_ROLE, "configuration", "ROLE", "DELETE"),

                // Configuration – Role–Permission
                new PermissionDefinition(READ_ROLE_PERMISSION,   "configuration", "ROLE_PERMISSION", "READ"),
                new PermissionDefinition(UPDATE_ROLE_PERMISSION, "configuration", "ROLE_PERMISSION", "UPDATE")
        );
    }
}
