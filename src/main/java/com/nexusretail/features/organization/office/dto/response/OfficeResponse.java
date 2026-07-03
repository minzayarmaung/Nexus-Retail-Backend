package com.nexusretail.features.organization.office.dto.response;

import lombok.Builder;

@Builder
public record OfficeResponse(
        Long officeId,
        String hierarchy
) {}