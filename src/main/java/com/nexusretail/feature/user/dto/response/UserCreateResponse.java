package com.nexusretail.feature.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserCreateResponse(

        @Schema(description = "The unique identifier of the user", example = "12345")
        long userId,

        @Schema(description = "The username of the user", example = "john_doe")
        String username,

        @Schema(description = "The email address of the user", example = "john.doe@example.com")
        String email,

        @Schema(description = "The authentication token for the user", example = "eyJhbGci")
        String token,

        @Schema(description = "Indicates if this is the user's first time login", example = "true")
        boolean isFirstTimeLogin,

        @Schema(description = "Indicates if the authentication token is expired", example = "false")
        boolean isExpired
) {}