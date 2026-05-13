package com.nexusretail.feature.configuration.role.service.impl;

import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.feature.configuration.role.dto.response.RoleResponse;
import com.nexusretail.feature.configuration.role.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Collection<RoleResponse> retrieveAllRoles(HttpServletRequest request) {
        return roleRepository.findAll()
                .stream()
                .map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .is_disabled(role.isDisabled())
                        .build())
                .toList();
    }
}
