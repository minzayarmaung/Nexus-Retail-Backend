package com.nexusretail.feature.merchant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Shop address response")
public record ShopAddressResponse(
        @Schema(description = "Address ID", example = "1")
        Long id,

        @Schema(description = "Address", example = "123 Main Street, Downtown")
        String address,

        @Schema(description = "Address type", example = "HEADQUARTER")
        String addressType,

        @Schema(description = "City", example = "Yangon")
        String city,

        @Schema(description = "State/Province", example = "Yangon Region")
        String state,

        @Schema(description = "Postal code", example = "11181")
        String postalCode,

        @Schema(description = "Country", example = "Myanmar")
        String country,

        @Schema(description = "Latitude for location services", example = "16.8661")
        Double latitude,

        @Schema(description = "Longitude for location services", example = "96.1951")
        Double longitude,

        @Schema(description = "Shop ID", example = "1")
        Long shopId,

        @Schema(description = "Created timestamp", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Updated timestamp", example = "2024-01-01T10:00:00")
        LocalDateTime updatedAt
) {}