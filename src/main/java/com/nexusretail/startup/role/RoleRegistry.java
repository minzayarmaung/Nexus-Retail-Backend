package com.nexusretail.startup.role;

import java.util.List;

public final class RoleRegistry {
    private RoleRegistry() {}

    public static List<RoleDefinition> all() {
        return List.of(
                new RoleDefinition("SYSTEM_ADMIN", "Full system access"),
                new RoleDefinition("OWNER",        "Business owner access")
        );
    }
}