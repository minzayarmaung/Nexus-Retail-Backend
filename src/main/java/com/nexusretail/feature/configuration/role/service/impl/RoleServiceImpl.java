package com.nexusretail.feature.configuration.role.service.impl;

import com.nexusretail.data.models.Permission;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.RolePermission;
import com.nexusretail.data.repositories.PermissionRepository;
import com.nexusretail.data.repositories.RolePermissionRepository;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.feature.configuration.permission.dto.response.PermissionResponse;
import com.nexusretail.feature.configuration.role.dto.request.RoleRequest;
import com.nexusretail.feature.configuration.role.dto.request.UpdateRolePermissionsRequest;
import com.nexusretail.feature.configuration.role.dto.response.RolePermissionResponse;
import com.nexusretail.feature.configuration.role.dto.response.RolePermissionUpdateResponse;
import com.nexusretail.feature.configuration.role.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Collection<RolePermissionResponse> retrieveAllRoles(HttpServletRequest request) {
        return roleRepository.findAll()
                .stream()
                .map(this::mapToRoleResponse)
                .toList();
    }

    @Override
    public RolePermissionResponse retrieveRoleById(Long id, HttpServletRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found with id: " + id));

        return mapToRoleResponse(role);
    }

    @Override
    public RolePermissionResponse retrieveRolePermissionsByRoleId(Long id, HttpServletRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));

        Set<Long> assignedPermissionIds = rolePermissionRepository.findPermissionIdsByRoleId(id);

        List<PermissionResponse> permissionResponses = permissionRepository.findAllOrderedByGrouping()
                .stream()
                .map(permission -> PermissionResponse.builder()
                        .grouping(permission.getGrouping())
                        .code(permission.getCode())
                        .entityName(permission.getEntityName())
                        .actionName(permission.getActionName())
                        .selected(assignedPermissionIds.contains(permission.getId()))
                        .build())
                .toList();

        return mapToRolePermissionResponse(role, permissionResponses);
    }

    @Override
    @Transactional
    public RolePermissionUpdateResponse updateRolePermissions(Long id, UpdateRolePermissionsRequest request) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));

        Set<String> incomingCodes = request.permissions().keySet();

        Map<String, Permission> permissionsByCode = permissionRepository.findAllByCodes(incomingCodes)
                .stream()
                .collect(Collectors.toMap(Permission::getCode, Function.identity()));

        Set<String> unknownCodes = incomingCodes.stream()
                .filter(code -> !permissionsByCode.containsKey(code))
                .collect(Collectors.toSet());

        if (!unknownCodes.isEmpty()) {
            throw new IllegalArgumentException("Unknown permission codes: " + unknownCodes);
        }

        Map<Long, RolePermission> existingByPermissionId = rolePermissionRepository.findAllByRoleId(id)
                .stream()
                .collect(Collectors.toMap(rp -> rp.getPermission().getId(), Function.identity()));

        Map<String, Boolean> changes = new LinkedHashMap<>();

        List<RolePermission> toAdd   = new ArrayList<>();
        Set<Long>           toRemove = new HashSet<>();

        for (Map.Entry<String, Boolean> entry : request.permissions().entrySet()) {
            String     code       = entry.getKey();
            boolean    selected   = entry.getValue();
            Permission permission = permissionsByCode.get(code);
            boolean    alreadyHas = existingByPermissionId.containsKey(permission.getId());

            if (selected && !alreadyHas) {
                toAdd.add(RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .build());
                changes.put(code, true);

            } else if (!selected && alreadyHas) {
                toRemove.add(permission.getId());
                changes.put(code, false);
            }
        }

        if (!toAdd.isEmpty()) {
            rolePermissionRepository.saveAll(toAdd);
        }

        if (!toRemove.isEmpty()) {
            rolePermissionRepository.deleteByRoleIdAndPermissionIds(id, toRemove);
        }

        return new RolePermissionUpdateResponse(
                role.getId(),
                role.getName(),
                new RolePermissionUpdateResponse.Changes(changes)
        );
    }

    @Override
    public RolePermissionResponse createRole(RoleRequest request) {

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
    public RolePermissionResponse updateRole(Long id, RoleRequest request) {
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

    private RolePermissionResponse mapToRoleResponse(Role role) {

        return RolePermissionResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .is_disabled(role.isDisabled())
                .build();
    }

    private RolePermissionResponse mapToRolePermissionResponse(Role role, List<PermissionResponse> permissions) {

        return RolePermissionResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .is_disabled(role.isDisabled())
                .permissionUsageData(permissions)
                .build();
    }
}
