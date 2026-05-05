package com.nexusretail.feature.configuration.managecode.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Create or Update CodeValue request")
public record CodeValueRequest(
        @Schema(description = "Code ID reference", example = "1", required = true)
        Long codeId,

        @Schema(description = "Value for the code option", example = "CREDIT_CARD", required = true)
        String value,

        @Schema(description = "Display name for UI", example = "Credit Card", required = true)
        String display,

        @Schema(description = "Detailed description", example = "Credit Card payment method")
        String description,

        @Schema(description = "Display order position", example = "1", required = true)
        Integer orderPosition,

        @Schema(description = "Status", example = "ACTIVE", required = true)
        Long status
) {}
