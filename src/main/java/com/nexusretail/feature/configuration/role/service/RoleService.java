package com.nexusretail.feature.configuration.role.service;

import com.nexusretail.feature.configuration.role.dto.request.RoleRequest;
import com.nexusretail.feature.configuration.role.dto.response.RoleResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collection;

public interface RoleService {
    Collection<RoleResponse> retrieveAllRoles(HttpServletRequest request);

    RoleResponse retrieveRoleById(Long id, HttpServletRequest request);

    RoleResponse createRole(RoleRequest roleRequest);

    RoleResponse updateRole(Long id, RoleRequest roleRequest);

    void deleteRole(Long id);
}
