package com.nexusretail.features.organization.holiday.dto.request;

import com.nexusretail.data.models.Office;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
public record HolidayDataRequest(
        String name,
        LocalDate fromDate,
        LocalDate toDate,
        LocalDate rescheduledTo,
        String description,
        Set<Office> officeId
) {}
