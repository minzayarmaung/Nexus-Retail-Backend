package com.nexusretail.feature.employee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Employee response")
public record EmployeeResponse(
        @Schema(description = "Employee ID", example = "1")
        Long id,

        @Schema(description = "Employee full name", example = "Aung Kyaw")
        String name,

        @Schema(description = "Employee email", example = "aung.kyaw@nexus.com")
        String email,

        @Schema(description = "Employee phone number", example = "(+959)945123456")
        String phoneNo,

        @Schema(description = "Employee date of birth", example = "2000-10-10")
        LocalDate dateOfBirth,

        @Schema(description = "Employee address", example = "No.12, Bogyoke Rd, Yangon")
        String address,

        @Schema(description = "Shop ID", example = "1")
        Long shopId,

        @Schema(description = "Employee position/role", example = "SALESPERSON")
        String position,

        @Schema(description = "Employee hire date", example = "2024-06-26")
        LocalDate hireDate,

        @Schema(description = "Employee service years", example = "1 year, 6 months, 15 days")
        String serviceYears,

        @Schema(description = "Employee salary/pay level", example = "PL3")
        String salary,

        @Schema(description = "Employee NRC (Myanmar National Registration Card)", example = "10/PaMaNa(N)980980")
        String nrc,

        @Schema(description = "Employee profile picture URL", example = "https://example.com/profiles/aung.jpg")
        String profilePictureUrl,

        @Schema(description = "Associated user ID", example = "1")
        Long userId,

        @Schema(description = "Associated user role", example = "SALESPERSON")
        String userRole,

        @Schema(description = "Created date", example = "2024-06-26T10:00:00")
        String createdAt,

        @Schema(description = "Last updated date", example = "2024-06-26T10:00:00")
        String updatedAt
) {}
