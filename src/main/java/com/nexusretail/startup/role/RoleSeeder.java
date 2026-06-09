package com.nexusretail.startup.role;

import com.nexusretail.data.models.Role;
import com.nexusretail.data.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.nexusretail.startup.core.SeederOrder.ROLES;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(ROLES)
public class RoleSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        int seeded = 0;
        for (RoleDefinition def : RoleRegistry.all()) {
            if (roleRepository.findByName(def.name()).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(def.name())
                        .build());
                seeded++;
            }
        }
        if (seeded > 0) log.info("Seeded {} new role(s).", seeded);
    }
}