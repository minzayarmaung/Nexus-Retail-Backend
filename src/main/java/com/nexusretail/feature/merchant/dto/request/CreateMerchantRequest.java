package com.nexusretail.feature.merchant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Create merchant Owner request")
public record CreateMerchantRequest(
        @Schema(description = "merchant owner's username", example = "john_owner", required = true)
        String username,

        @Schema(description = "merchant owner's first name", example = "John")
        String name,

        @Schema(description = "merchant owner's profile picture URL", example = "https://example.com/profile.jpg")
        String profileUrl,

        @Schema(description = "merchant owner's email", example = "john.owner@merchant.com", required = true)
        String email,

        @Schema(description = "merchant owner's password", example = "password123", required = true)
        String password,

        @Schema(description = "merchant owner's phone number", example = "+1234567890", required = true)
        String phoneNo,

        @Schema(description = "shop ID to assign to the owner", example = "1", required = true)
        Long shopId
) {}
