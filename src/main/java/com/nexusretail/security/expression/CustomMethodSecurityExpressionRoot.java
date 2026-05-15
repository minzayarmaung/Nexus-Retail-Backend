package com.nexusretail.security.expression;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot
        extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    /**
     * The method you write in @PreAuthorize("hasPermission('READ_CODE')").
     *
     * Logic (mirrors Fineract):
     *  - ALL_FUNCTIONS          → can do anything
     *  - ALL_FUNCTIONS_READ     → can do any READ_* permission
     *  - exact code match       → specific grant
     */
    public boolean hasPermission(String permissionCode) {
        if (hasAuthority("ALL_FUNCTIONS")) return true;
        if (permissionCode.startsWith("READ_") && hasAuthority("ALL_FUNCTIONS_READ")) return true;
        return hasAuthority(permissionCode);
    }

    // ── MethodSecurityExpressionOperations boilerplate ───────────────────────

    @Override public void setFilterObject(Object o)  { this.filterObject = o; }
    @Override public Object getFilterObject()         { return filterObject; }
    @Override public void setReturnObject(Object o)  { this.returnObject = o; }
    @Override public Object getReturnObject()         { return returnObject; }
    @Override public Object getThis()                 { return this; }
}