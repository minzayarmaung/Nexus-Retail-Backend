package com.nexusretail.system.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record UserUpdateRequest(
        @Schema(description = "Unique username for the user", example = "john_doe" , nullable = false)
        String username,

        @Schema(description = "Email address of the user", example = "john.doe@example.com", nullable = false)
        String email,

        @Schema(description = "First name of the user", example = "John", nullable = false)
        String firstName,

        @Schema(description = "Last name of the user", example = "Doe", nullable = false)
        String lastName,

        @Schema(description = "Flag to indicate if user can change password", example = "false", nullable = false)
        boolean cannotChangePassword,

        @Schema(description = "User Roles" , example = "[\"USER\"]" , nullable = false)
        List<String> roles
) {}
