package com.nexusretail.feature.merchant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Update shop address request (PATCH)")
public record UpdateShopAddressRequest(
        @Schema(description = "Address ID (required for updates)", example = "1")
        Long id,

        @Schema(description = "Address")
        String address,

        @Schema(description = "Address type")
        String addressType,

        @Schema(description = "City")
        String city,

        @Schema(description = "State/Province")
        String state,

        @Schema(description = "Postal code")
        String postalCode,

        @Schema(description = "Country")
        String country,

        @Schema(description = "Latitude for location services")
        Double latitude,

        @Schema(description = "Longitude for location services")
        Double longitude
) {}
