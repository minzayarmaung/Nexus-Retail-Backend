package com.nexusretail.security.permission;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PermissionCode {
    // ─── Special ────────────────────────────────────────────────────────────
    public static final String ALL_FUNCTIONS        = "ALL_FUNCTIONS";
    public static final String ALL_FUNCTIONS_READ   = "ALL_FUNCTIONS_READ";

    // ─── Configuration : Codes ───────────────────────────────────────────────
    public static final String READ_CODE   = "READ_CODE";
    public static final String CREATE_CODE = "CREATE_CODE";
    public static final String UPDATE_CODE = "UPDATE_CODE";
    public static final String DELETE_CODE = "DELETE_CODE";

    // ─── Configuration : Roles ───────────────────────────────────────────────
    public static final String READ_ROLE   = "READ_ROLE";
    public static final String CREATE_ROLE = "CREATE_ROLE";
    public static final String UPDATE_ROLE = "UPDATE_ROLE";
    public static final String DELETE_ROLE = "DELETE_ROLE";

    // ─── Configuration : Role–Permission assignment ──────────────────────────
    public static final String READ_ROLE_PERMISSION   = "READ_ROLE_PERMISSION";
    public static final String UPDATE_ROLE_PERMISSION = "UPDATE_ROLE_PERMISSION";
}
