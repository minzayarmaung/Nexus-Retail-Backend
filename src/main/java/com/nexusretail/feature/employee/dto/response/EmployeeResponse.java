package com.nexusretail.feature.employee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Employee response")
public record EmployeeResponse(
        @Schema(description = "Employee ID", example = "1")
        Long id,

        @Schema(description = "Employee first name", example = "John")
        String firstName,

        @Schema(description = "Employee last name", example = "Doe")
        String lastName,

        @Schema(description = "Employee full name", example = "John Doe")
        String fullName,

        @Schema(description = "Employee email", example = "john.doe@shop.com")
        String email,

        @Schema(description = "Employee phone number", example = "+1234567890")
        String phoneNo,

        @Schema(description = "Employee date of birth", example = "1990-01-15")
        LocalDate dateOfBirth,

        @Schema(description = "Employee address", example = "123 Main St, City, State")
        String address,

        @Schema(description = "Shop ID", example = "1")
        Long shopId,

        @Schema(description = "Employee position/role", example = "SALESPERSON")
        String position,

        @Schema(description = "Employee hire date", example = "2023-01-01")
        LocalDate hireDate,

        @Schema(description = "Employee service years", example = "1 year, 6 months, 15 days")
        String serviceYears,

        @Schema(description = "Employee salary", example = "50000.00")
        String salary,

        @Schema(description = "Employee NRC (Myanmar National Registration Card)", example = "12/LaKaNa(N)318903")
        String nrc,

        @Schema(description = "Employee profile picture URL", example = "https://example.com/profiles/john.jpg")
        String profilePictureUrl,

        @Schema(description = "Associated user ID", example = "1")
        Long userId,

        @Schema(description = "Associated user role", example = "SALESPERSON")
        String userRole,

        @Schema(description = "Created date", example = "2023-01-01T10:00:00")
        String createdAt,

        @Schema(description = "Last updated date", example = "2023-01-01T10:00:00")
        String updatedAt
) {}
