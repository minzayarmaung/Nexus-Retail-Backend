package com.nexusretail.startup.office;

import java.time.LocalDate;
import java.util.List;

public final class OfficeRegistry {
    private OfficeRegistry() {}

    public static List<OfficeDefinition> all() {
        return List.of(
                new OfficeDefinition("HEAD_OFFICE", null, LocalDate.EPOCH)
        );
    }
}