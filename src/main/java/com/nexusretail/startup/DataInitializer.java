package com.nexusretail.startup;

import com.nexusretail.common.constant.Status;
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

        // Create ADMIN role if it doesn't exist
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name("ADMIN")
                            .build();
                    return roleRepository.save(role);
                });

        log.info("ADMIN role ready: {}", adminRole.getName());

        // Create system admin user if it doesn't exist
        if (userRepository.findByUsername("nexus").isEmpty()) {
            User adminUser = User.builder()
                    .username("nexus")
                    .email("nexusretail@gmail.com")
                    .createdAt(java.time.LocalDateTime.now())
                    .updatedAt(java.time.LocalDateTime.now())
                    .status(Status.ACTIVE)
                    .password(passwordEncoder.encode("password"))
                    .role(adminRole)
                    .phoneNo("+1234567890") // Default phone number
                    .build();

            userRepository.save(adminUser);
            log.info("System admin user created: {}", adminUser.getUsername());
        } else {
            log.info("System admin user already exists");
        }

        log.info("Data initialization completed successfully");
    }
}
