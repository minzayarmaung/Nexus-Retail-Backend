package com.nexusretail.feature.configuration.template.dto.response;

import lombok.Builder;

@Builder
public record TemplateDataResponse(
        String name,
        String description
){}
