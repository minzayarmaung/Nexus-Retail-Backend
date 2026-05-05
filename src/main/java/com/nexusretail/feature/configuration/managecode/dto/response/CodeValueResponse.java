package com.nexusretail.feature.configuration.managecode.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "CodeValue response")
public record CodeValueResponse(
        @Schema(description = "CodeValue ID", example = "1")
        Long id,

        @Schema(description = "Code ID reference", example = "1")
        Long codeId,

        @Schema(description = "Value for the code option", example = "CREDIT_CARD")
        String value,

        @Schema(description = "Display name for UI", example = "Credit Card")
        String display,

        @Schema(description = "Detailed description", example = "Credit Card payment method")
        String description,

        @Schema(description = "Display order position", example = "1")
        Integer orderPosition,

        @Schema(description = "Status", example = "ACTIVE")
        String status
) {}
