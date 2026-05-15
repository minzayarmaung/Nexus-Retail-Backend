package com.nexusretail.feature.configuration.role.service.impl;

import com.nexusretail.data.models.Permission;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.RolePermission;
import com.nexusretail.data.repositories.RolePermissionRepository;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.feature.configuration.role.dto.request.RoleRequest;
import com.nexusretail.feature.configuration.role.dto.response.RoleResponse;
import com.nexusretail.feature.configuration.role.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public Collection<RoleResponse> retrieveAllRoles(HttpServletRequest request) {
        return roleRepository.findAll()
                .stream()
                .map(this::mapToRoleResponse)
                .toList();
    }

    @Override
    public RoleResponse retrieveRoleById(Long id, HttpServletRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found with id: " + id));

        List<Permission> permissions = rolePermissionRepository.findByRole(role)
                .stream()
                .map(RolePermission::getPermission)
                .toList();

        return mapToRolePermissionResponse(role , permissions);
    }

    @Override
    public RoleResponse createRole(RoleRequest request) {

        try {
            if (roleRepository.existsByName(request.name())) {
                throw new RuntimeException(
                        "Role already exists with name: " + request.name()
                );
            }

            Role role = Role.builder()
                    .name(request.name())
                    .description(request.description())
                    .disabled(request.is_disabled())
                    .build();

            Role savedRole = roleRepository.save(role);
            return mapToRoleResponse(savedRole);

        } catch (Exception ex) {
            log.error("Failed to create role", ex);
            throw new RuntimeException(
                    "Unable to create role: " + ex.getMessage()
            );
        }
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest request) {
        try {

            Role role = roleRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Role not found with id: " + id
                            )
                    );

            boolean roleNameExists =
                    roleRepository.existsByName(request.name());

            if (roleNameExists &&
                    !role.getName().equals(request.name())) {

                throw new BadRequestException(
                        "Role already exists with name: " + request.name()
                );
            }

            role.setName(request.name());
            role.setDescription(request.description());
            role.setDisabled(request.is_disabled());

            Role updatedRole = roleRepository.save(role);

            return mapToRoleResponse(updatedRole);

        } catch (Exception ex) {

            log.error("Failed to update role", ex);

            throw new RuntimeException(
                    "Unable to update role"
            );
        }
    }

    @Override
    public void deleteRole(Long id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Role not found with id: " + id
                            )
                    );

            roleRepository.delete(role);

        } catch (Exception ex) {
            log.error("Failed to delete role", ex);
            throw new RuntimeException(
                    "Unable to delete role: " + ex.getMessage()
            );
        }
    }

    private RoleResponse mapToRoleResponse(Role role) {

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .is_disabled(role.isDisabled())
                .build();
    }

    private RoleResponse mapToRolePermissionResponse(Role role, List<Permission> permissions) {

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .is_disabled(role.isDisabled())
                .permissionUsageData(permissions)
                .build();
    }
}
