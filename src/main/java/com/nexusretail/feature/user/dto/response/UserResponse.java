package com.nexusretail.feature.user.dto.response;

import com.nexusretail.feature.merchant.dto.response.ShopResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "User details returned by the API")
public record UserResponse(

        @Schema(description = "Unique user identifier", example = "1")
        Long id,

        @Schema(description = "Login username", example = "john_doe")
        String username,

        @Schema(description = "Email address", example = "john@example.com")
        String email,

        @Schema(description = "Phone number", example = "+66812345678")
        String phoneNo,

        @Schema(description = "URL of the user's profile photo")
        String profilePhotoUrl,

        @Schema(description = "Assigned role name", example = "ADMIN")
        String role,

        @Schema(description = "Owned shop — present only if user is the shop owner, null otherwise")
        ShopResponse shop
) {}