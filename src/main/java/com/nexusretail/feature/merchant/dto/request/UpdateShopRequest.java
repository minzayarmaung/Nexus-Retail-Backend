package com.nexusretail.feature.merchant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Update shop request (PATCH)")
public record UpdateShopRequest(
        @Schema(description = "Shop name")
        String name,

        @Schema(description = "Shop type")
        String type,

        @Schema(description = "Shop phone number")
        String phoneNo,

        @Schema(description = "Shop photo URL")
        String shopPhotoUrl,

        @Schema(description = "Owner ID (User ID of the shop owner)")
        Long ownerId,

        @Schema(description = "Shop addresses to update")
        List<UpdateShopAddressRequest> addresses
) {}