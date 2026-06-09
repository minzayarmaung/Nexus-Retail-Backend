package com.nexusretail.startup.code;

import com.nexusretail.data.models.Code;
import com.nexusretail.data.models.CodeValue;
import com.nexusretail.data.repositories.CodeRepository;
import com.nexusretail.data.repositories.CodeValueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.nexusretail.startup.core.SeederOrder.CODES;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(CODES)
public class CodeSeeder implements ApplicationRunner {

    private final CodeRepository     codeRepository;
    private final CodeValueRepository codeValueRepository;

    @Override
    public void run(ApplicationArguments args) {
        int seededValues = 0;

        for (CodeDefinition def : CodeRegistry.all()) {
            Code code = codeRepository.findByCodeType(def.codeType())
                    .orElseGet(() -> codeRepository.save(
                            Code.builder()
                                    .codeType(def.codeType())
                                    .description(def.description())
                                    .build()
                    ));

            for (CodeValueEntry entry : def.values()) {
                if (!codeValueRepository.existsByCodeIdAndValue(code.getId(), entry.value())) {
                    codeValueRepository.save(
                            CodeValue.builder()
                                    .code(code)
                                    .value(entry.value())
                                    .display(entry.displayName())
                                    .orderPosition(entry.orderPosition())
                                    .build()
                    );
                    seededValues++;
                }
            }
        }

        if (seededValues > 0) log.info("Seeded {} new code value(s).", seededValues);
    }
}