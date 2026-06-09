package com.nexusretail.startup.rolepermission;

import com.nexusretail.data.models.Permission;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.RolePermission;
import com.nexusretail.data.repositories.PermissionRepository;
import com.nexusretail.data.repositories.RolePermissionRepository;
import com.nexusretail.data.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.nexusretail.startup.core.SeederOrder.ROLE_PERMS;
import static com.nexusretail.startup.permission.PermissionCode.ALL_FUNCTIONS;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(ROLE_PERMS)
public class RolePermissionSeeder implements ApplicationRunner {

    private final RoleRepository           roleRepository;
    private final PermissionRepository     permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public void run(ApplicationArguments args) {
        Role adminRole = roleRepository.findByName("SYSTEM_ADMIN")
                .orElseThrow(() -> new IllegalStateException("SYSTEM_ADMIN not found"));

        Permission allFunctions = permissionRepository.findByCode(ALL_FUNCTIONS)
                .orElseThrow(() -> new IllegalStateException("ALL_FUNCTIONS not found — PermissionSeeder must run first"));

        if (rolePermissionRepository.existsByRoleIdAndPermissionId(
                adminRole.getId(), allFunctions.getId())) return;

        rolePermissionRepository.save(RolePermission.of(adminRole, allFunctions));
        log.info("Assigned ALL_FUNCTIONS to SYSTEM_ADMIN.");
    }
}