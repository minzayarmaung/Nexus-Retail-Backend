package com.nexusretail.features.system.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record UserCreateRequest(

        @Schema(description = "Unique username for the user", example = "john_doe" , nullable = false)
        String username,

        @Schema(description = "Email address of the user", example = "john.doe@example.com", nullable = false)
        String email,

        @Schema(description = "First name of the user", example = "John", nullable = false)
        String firstName,

        @Schema(description = "Last name of the user", example = "Doe", nullable = false)
        String lastName,

        @Schema(description = "Password for the user", example = "SecurePass123", nullable = true)
        String password,

        @Schema(description = "Flag to indicate if password should be generated", example = "true", nullable = false)
        boolean generatePassword,

        @Schema(description = "Flag to indicate if user can change password", example = "false", nullable = false)
        boolean cannotChangePassword,

        @Schema(description = "User Roles" , example = "false" , nullable = false)
        List<String> roles
) {}