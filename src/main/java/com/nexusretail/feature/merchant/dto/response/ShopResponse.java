package com.nexusretail.feature.merchant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "Shop response")
public record ShopResponse(
        @Schema(description = "Shop ID", example = "1")
        Long id,

        @Schema(description = "Shop name", example = "Nexus Retail Store")
        String name,

        @Schema(description = "Shop type", example = "RETAIL")
        String type,

        @Schema(description = "Shop phone number", example = "+1234567890")
        String phoneNo,

        @Schema(description = "Shop photo URL", example = "https://example.com/shop-photo.jpg")
        String shopPhotoUrl,

        @Schema(description = "Owner ID", example = "1")
        Long ownerId,

        @Schema(description = "Owner name", example = "John Doe")
        String ownerName,

        @Schema(description = "Shop addresses")
        List<ShopAddressResponse> addresses,

        @Schema(description = "Created timestamp", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Updated timestamp", example = "2024-01-01T10:00:00")
        LocalDateTime updatedAt
) {}
