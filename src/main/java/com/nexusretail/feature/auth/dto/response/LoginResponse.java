package com.nexusretail.feature.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Login response payload")
public record LoginResponse(
        @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "User's username", example = "john_doe")
        String username,

        @Schema(description = "User's email address", example = "john@example.com")
        String email,

        @Schema(description = "User's role in the system", example = "ADMIN")
        String role,

        @Schema(description = "User's unique identifier", example = "1")
        Long userId
) {}
