package com.nexusretail.startup.office;

import java.time.LocalDate;

public record OfficeDefinition(
        String name,
        String parentName,
        LocalDate openingDate
) {}