package com.nexusretail.feature.shop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Create Shop Owner request")
public record CreateShopOwnerRequest(
        @Schema(description = "Shop owner's username", example = "john_owner", required = true)
        String username,

        @Schema(description = "Shop owner's email", example = "john.owner@shop.com", required = true)
        String email,

        @Schema(description = "Shop owner's password", example = "password123", required = true)
        String password,

        @Schema(description = "Shop owner's phone number", example = "+1234567890", required = true)
        String phoneNo,

        @Schema(description = "Shop ID to assign to the owner", example = "1", required = true)
        Long shopId,

        @Schema(description = "Shop owner's first name", example = "John")
        String firstName,

        @Schema(description = "Shop owner's last name", example = "Owner")
        String lastName
) {}
