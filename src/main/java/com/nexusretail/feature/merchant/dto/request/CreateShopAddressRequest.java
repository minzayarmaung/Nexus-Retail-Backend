package com.nexusretail.feature.merchant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Create shop address request")
public record CreateShopAddressRequest(
        @Schema(description = "Address", example = "123 Main Street, Downtown", required = true)
        String address,

        @Schema(description = "Address type", example = "HEADQUARTER", required = true)
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
        Double longitude
) {}