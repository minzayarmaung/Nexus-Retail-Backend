package com.nexusretail.feature.employee.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Create or Update Employee request")
public record EmployeeRequest(
        @Schema(description = "Employee full name", example = "Aung Kyaw", required = true)
        String name,

        @Schema(description = "Employee email", example = "aung.kyaw@nexus.com", required = true)
        String email,

        @Schema(description = "Employee phone number", example = "(+959)945123456", required = true)
        String phoneNo,

        @Schema(description = "Employee date of birth", example = "2000-10-10")
        LocalDate dateOfBirth,

        @Schema(description = "Employee address", example = "No.12, Bogyoke Rd, Yangon")
        String address,

        @Schema(description = "Employee position/role", example = "SALESPERSON")
        String position,

        @Schema(description = "Employee hire date", example = "2024-06-26")
        LocalDate hireDate,

        @Schema(description = "Employee pay level", example = "PL3")
        String payLevel,

        @Schema(description = "Employee NRC (Myanmar National Registration Card)", example = "10/PaMaNa(N)980980")
        String nrc,

        @Schema(description = "Employee profile picture URL", example = "https://example.com/profiles/aung.jpg")
        String profilePicUrl,

        @Schema(description = "Create user account for employee", example = "true")
        Boolean createUserAccount,

        @Schema(description = "User role if creating account", example = "SALESPERSON")
        String userRole,

        @Schema(description = "Username for user account", example = "aung_kyaw")
        String username,

        @Schema(description = "Password for user account", example = "password123")
        String password
) {}
