package com.nexusretail.feature.configuration.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Create or Update Code request")
public record CodeRequest(
        @Schema(description = "Unique code type identifier", example = "PAYMENT_METHOD")
        String codeType,

        @Schema(description = "Code description", example = "Payment method types")
        String description
) {}
