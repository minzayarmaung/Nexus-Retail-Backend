package com.nexusretail.feature.configuration.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Code response")
public record CodeResponse(
        @Schema(description = "Code ID", example = "1")
        Long id,

        @Schema(description = "Unique code type identifier", example = "PAYMENT_METHOD")
        String codeType,

        @Schema(description = "Code description", example = "Payment method types")
        String description,

        @Schema(description = "Status", example = "ACTIVE")
        String status
) {}
