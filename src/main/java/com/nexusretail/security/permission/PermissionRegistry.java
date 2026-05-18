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
                new PermissionDefinition(ALL_FUNCTIONS,      "special", "All Functions",   null),
                new PermissionDefinition(ALL_FUNCTIONS_READ, "special", "All Functions Read",   null),

                // Configuration – Codes
                new PermissionDefinition(READ_CODE,   "configuration", "Read Code", "READ"),
                new PermissionDefinition(CREATE_CODE, "configuration", "Create Code", "CREATE"),
                new PermissionDefinition(UPDATE_CODE, "configuration", "Update Code", "UPDATE"),
                new PermissionDefinition(DELETE_CODE, "configuration", "Delete Code", "DELETE"),

                // Configuration – Roles
                new PermissionDefinition(READ_ROLE,   "configuration", "Read Role", "READ"),
                new PermissionDefinition(CREATE_ROLE, "configuration", "Create Role", "CREATE"),
                new PermissionDefinition(UPDATE_ROLE, "configuration", "Update Role", "UPDATE"),
                new PermissionDefinition(DELETE_ROLE, "configuration", "Delete Role", "DELETE"),

                // Configuration – Role–Permission
                new PermissionDefinition(READ_ROLE_PERMISSION,   "configuration", "Read Role Permission", "READ"),
                new PermissionDefinition(UPDATE_ROLE_PERMISSION, "configuration", "Update Role Permissionb", "UPDATE")
        );
    }
}
