package com.nexusretail.startup.passwordpolicy;

import com.nexusretail.data.models.PasswordValidationPolicy;
import com.nexusretail.data.repositories.PasswordValidationPolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.nexusretail.startup.core.SeederOrder.PASSWORD_POLICY;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(PASSWORD_POLICY)
public class PasswordPolicySeeder implements ApplicationRunner {

    private final PasswordValidationPolicyRepository passwordValidationPolicyRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int seeded = 0;

        for(PasswordPolicyDefinition pp : PasswordPolicyRegistry.all()) {
            if (!passwordValidationPolicyRepository.existsByKey(pp.key())) {
                passwordValidationPolicyRepository.save(
                        PasswordValidationPolicy.builder()
                                .regex(pp.regex())
                                .description(pp.description())
                                .active(pp.active())
                                .key(pp.key())
                                .build()
                );
                seeded++;
            }
        }
        log.info("Seeded {} password validation policies", seeded);
    }
}
