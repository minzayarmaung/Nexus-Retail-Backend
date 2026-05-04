package com.nexusretail.feature.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Login request payload")
public record LoginRequest(
        @Schema(description = "Username for authentication (case sensitive)", example = "john_doe")
        String username,

        @Schema(description = "Email for authentication (alternative to username)", example = "john@example.com")
        String email,

        @Schema(description = "User password", example = "password123", required = true)
        String password
) {}
