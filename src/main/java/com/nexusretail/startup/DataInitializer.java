package com.nexusretail.startup;

import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.RolePermission;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.PermissionRepository;
import com.nexusretail.data.repositories.RolePermissionRepository;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing system data...");

        createRoles();

        createSystemAdminUser();

        createRolePermissions();

        log.info("Data initialization completed successfully");
    }

    private void createRoles() {
        createRoleIfNotExists("SYSTEM_ADMIN", null);

        createRoleIfNotExists("OWNER", null);

        log.info("All system roles initialized");
    }

    private void createRoleIfNotExists(String roleName, Long shopId) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = Role.builder()
                    .name(roleName)
                    .build();
            roleRepository.save(role);
            log.info("Role created: {}", roleName);
        } else {
            log.info("Role already exists: {}", roleName);
        }
    }

    private void createSystemAdminUser() {
        if (userRepository.findByUsername("nexus").isEmpty()) {
            Role adminRole = roleRepository.findByName("SYSTEM_ADMIN")
                    .orElseThrow(() -> new RuntimeException("SYSTEM_ADMIN role not found"));

            User adminUser = User.builder()
                    .username("nexus")
                    .firstName("first")
                    .lastName("last")
                    .email("nexusretail@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(adminUser);
            log.info("System admin user created: {}", adminUser.getUsername());
        } else {
            log.info("System admin user already exists");
        }
    }

    public void createRolePermissions() {

        var permissionOpt = permissionRepository.findByCode("ALL_FUNCTIONS");

        if (permissionOpt.isEmpty()) {
            log.warn("Permission ALL_FUNCTIONS not found");
            return;
        }

        Role adminRole = roleRepository.findByName("SYSTEM_ADMIN")
                .orElseThrow(() -> new RuntimeException("SYSTEM_ADMIN role not found"));

        boolean exists = rolePermissionRepository
                .existsByRoleIdAndPermissionId(adminRole.getId(), permissionOpt.get().getId()); // ← match @IdClass fields

        if (exists) {
            log.info("Role permission already exists");
            return;
        }

        RolePermission rolePermission = RolePermission.of(adminRole, permissionOpt.get()); // ← use factory, not builder

        rolePermissionRepository.save(rolePermission);

        log.info("Role permission created");
    }
}
