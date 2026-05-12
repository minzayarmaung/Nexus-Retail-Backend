package com.nexusretail.startup;

import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing system data...");

        // Create roles
        createRoles();

        // Create system admin user if it doesn't exist
        createSystemAdminUser();

        log.info("Data initialization completed successfully");
    }

    private void createRoles() {
        // SYSTEM_ADMIN role (global, no shopId)
        createRoleIfNotExists("SYSTEM_ADMIN", null);

        // OWNER role (will be created per shop)
        createRoleIfNotExists("OWNER", null); // Template role, actual owners will have shopId

        // HR role (will be created per shop)
        createRoleIfNotExists("HR", null); // Template role, actual HR will have shopId

        // SALESPERSON role (will be created per shop)
        createRoleIfNotExists("SALESPERSON", null); // Template role, actual salespeople will have shopId

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
                    .email("nexusretail@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .role(adminRole)
                    .phoneNo("+1234567890")
                    .build();

            userRepository.save(adminUser);
            log.info("System admin user created: {}", adminUser.getUsername());
        } else {
            log.info("System admin user already exists");
        }
    }
}
