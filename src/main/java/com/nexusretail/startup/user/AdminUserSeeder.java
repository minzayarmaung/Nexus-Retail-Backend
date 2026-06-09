package com.nexusretail.startup.user;

import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import static com.nexusretail.startup.core.SeederOrder.USERS;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(USERS)
public class AdminUserSeeder implements ApplicationRunner {

    private final UserRepository   userRepository;
    private final RoleRepository   roleRepository;
    private final PasswordEncoder  passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByUsername("nexus").isPresent()) return;

        Role adminRole = roleRepository.findByName("SYSTEM_ADMIN")
                .orElseThrow(() -> new IllegalStateException(
                        "SYSTEM_ADMIN not found — RoleSeeder must run first (check @Order)"));

        userRepository.save(User.builder()
                .username("nexus")
                .firstName("Nexus")
                .lastName("Admin")
                .email("nexusretail@gmail.com")
                .password(passwordEncoder.encode("password"))
                .roles(Set.of(adminRole))
                .build());

        log.info("Admin user 'nexus' created.");
    }
}