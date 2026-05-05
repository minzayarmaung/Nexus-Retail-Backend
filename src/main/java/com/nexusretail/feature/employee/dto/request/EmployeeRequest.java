package com.nexusretail.feature.employee.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Create or Update Employee request")
public record EmployeeRequest(
        @Schema(description = "Employee first name", example = "John", required = true)
        String firstName,

        @Schema(description = "Employee last name", example = "Doe", required = true)
        String lastName,

        @Schema(description = "Employee email", example = "john.doe@shop.com", required = true)
        String email,

        @Schema(description = "Employee phone number", example = "+1234567890", required = true)
        String phoneNo,

        @Schema(description = "Employee date of birth", example = "1990-01-15")
        LocalDate dateOfBirth,

        @Schema(description = "Employee address", example = "123 Main St, City, State")
        String address,

        @Schema(description = "Employee position/role", example = "SALESPERSON")
        String position,

        @Schema(description = "Employee hire date", example = "2023-01-01")
        LocalDate hireDate,

        @Schema(description = "Employee salary", example = "50000.00")
        String salary,

        @Schema(description = "Employee NRC (Myanmar National Registration Card)", example = "12/LaKaNa(N)318903")
        String nrc,

        @Schema(description = "Employee profile picture URL", example = "https://example.com/profiles/john.jpg")
        String profilePictureUrl,

        @Schema(description = "Create user account for employee", example = "true")
        Boolean createUserAccount,

        @Schema(description = "User role if creating account", example = "SALESPERSON")
        String userRole,

        @Schema(description = "Username for user account", example = "john_doe")
        String username,

        @Schema(description = "Password for user account", example = "password123")
        String password
) {}
