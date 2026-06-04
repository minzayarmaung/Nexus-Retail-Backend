package com.nexusretail.features.configuration.template.dto.response;

import lombok.Builder;

@Builder
public record TemplateDataResponse(
        String name,
        String description
){}
