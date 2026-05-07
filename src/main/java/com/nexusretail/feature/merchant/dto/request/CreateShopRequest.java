package com.nexusretail.feature.merchant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Create shop request")
public record CreateShopRequest(
        @Schema(description = "Shop name", example = "Nexus Retail Store", required = true)
        String name,

        @Schema(description = "Shop type", example = "RETAIL", required = true)
        String type,

        @Schema(description = "Shop phone number", example = "+1234567890")
        String phoneNo,

        @Schema(description = "Shop photo URL", example = "https://example.com/shop-photo.jpg")
        String shopPhotoUrl,

//        @Schema(description = "Owner ID (User ID of the shop owner)", example = "1", required = true)
//        Long ownerId,

        @Schema(description = "Shop addresses", required = true)
        List<CreateShopAddressRequest> addresses
) {}
