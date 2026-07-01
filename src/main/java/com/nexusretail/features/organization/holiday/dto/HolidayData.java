package com.nexusretail.features.organization.holiday.dto;

import lombok.Builder;

@Builder
public record HolidayData(
        Long id,
        String name,
        String description,
        String fromDate,
        String toDate,
        String rescheduledTo
) {}