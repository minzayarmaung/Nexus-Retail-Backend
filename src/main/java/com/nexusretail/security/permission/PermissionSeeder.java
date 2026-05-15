package com.nexusretail.security.permission;

import com.nexusretail.data.models.Permission;
import com.nexusretail.data.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionSeeder implements ApplicationRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(ApplicationArguments args) {
        int seeded = 0;

        for (PermissionDefinition def : PermissionRegistry.all()) {
            if (!permissionRepository.existsByCode(def.code())) {
                permissionRepository.save(
                        Permission.builder()
                                .code(def.code())
                                .grouping(def.grouping())
                                .entityName(def.entityName())
                                .actionName(def.actionName())
                                .canMakerChecker(false)
                                .build()
                );
                seeded++;
            }
        }

        if (seeded > 0) {
            log.info("Seeded {} new permission(s).", seeded);
        }
    }
}
