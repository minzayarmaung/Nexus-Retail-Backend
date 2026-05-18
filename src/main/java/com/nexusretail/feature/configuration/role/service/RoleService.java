package com.nexusretail.feature.configuration.role.service;

import com.nexusretail.feature.configuration.role.dto.request.RoleRequest;
import com.nexusretail.feature.configuration.role.dto.request.UpdateRolePermissionsRequest;
import com.nexusretail.feature.configuration.role.dto.response.RolePermissionResponse;
import com.nexusretail.feature.configuration.role.dto.response.RolePermissionUpdateResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collection;

public interface RoleService {
    Collection<RolePermissionResponse> retrieveAllRoles(HttpServletRequest request);

    RolePermissionResponse retrieveRoleById(Long id, HttpServletRequest request);

    RolePermissionResponse createRole(RoleRequest roleRequest);

    RolePermissionResponse updateRole(Long id, RoleRequest roleRequest);

    void deleteRole(Long id);

    RolePermissionResponse retrieveRolePermissionsByRoleId(Long id, HttpServletRequest request);

    RolePermissionUpdateResponse updateRolePermissions(Long id , UpdateRolePermissionsRequest request);
}
