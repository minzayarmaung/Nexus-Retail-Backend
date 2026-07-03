package com.nexusretail.features.organization.office.dto.request;

import lombok.Builder;

@Builder
public record OfficeRequest(
    String name,
    String openingDate,
    String parentId
) {}
