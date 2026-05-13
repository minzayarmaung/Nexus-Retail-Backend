package com.nexusretail.feature.configuration.role.service.impl;

import com.nexusretail.data.models.Role;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

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

        return mapToRoleResponse(role);
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

    private RoleResponse mapToRoleResponse(Role role) {

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .is_disabled(role.isDisabled())
                .build();
    }
}
