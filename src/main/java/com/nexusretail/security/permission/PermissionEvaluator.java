package com.nexusretail.security.permission;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    /**
     * Called by: hasPermission(null, 'READ_CODE')
     *   target     = null  (we don't need a target object, just the code)
     *   permission = "READ_CODE"
     *
     * Resolution order (mirrors Fineract):
     *   1. ALL_FUNCTIONS           → allow everything
     *   2. ALL_FUNCTIONS_READ      → allow any READ_* code
     *   3. exact code match        → specific grant
     *
     */

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(PermissionEvaluator.class);

    @Override
    public boolean hasPermission(Authentication auth, Object target, Object permission) {

        // ── ADD THESE LOGS ──────────────────────────────────────────────────
        log.info("=== PermissionEvaluator called ===");
        log.info("Permission requested: {}", permission);
        log.info("Auth is null: {}", auth == null);
        if (auth != null) {
            log.info("Auth name: {}", auth.getName());
            log.info("Authorities: {}", auth.getAuthorities());
        }
        // ────────────────────────────────────────────────────────────────────

        if (auth == null || permission == null) return false;

        String code = permission.toString();

        return hasAuthority(auth, "ALL_FUNCTIONS")
                || (code.startsWith("READ_") && hasAuthority(auth, "ALL_FUNCTIONS_READ"))
                || hasAuthority(auth, code);
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId,
                                 String targetType, Object permission) {
        return hasPermission(auth, targetId, permission);
    }

    private boolean hasAuthority(Authentication auth, String authority) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }
}