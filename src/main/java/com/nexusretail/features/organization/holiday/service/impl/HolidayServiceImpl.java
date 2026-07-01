package com.nexusretail.features.organization.holiday.service.impl;

import com.nexusretail.data.models.Holiday;
import com.nexusretail.data.repositories.HolidayRespository;
import com.nexusretail.features.organization.holiday.dto.HolidayData;
import com.nexusretail.features.organization.holiday.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRespository holidayRepository;

    @Override
    public Collection<HolidayData> retrieveAllHolidaysBySearchParameters(
            Long officeId,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        return holidayRepository
                .findAllBySearchParameters(officeId, fromDate, toDate)
                .stream()
                .map(this::mapToHolidayData)
                .toList();
    }

    private HolidayData mapToHolidayData(Holiday holiday) {
        return HolidayData.builder()
                .id(holiday.getId())
                .name(holiday.getName())
                .description(holiday.getDescription())
                .fromDate(
                        holiday.getFromDate() != null
                                ? holiday.getFromDate().toString()
                                : null
                )
                .toDate(
                        holiday.getToDate() != null
                                ? holiday.getToDate().toString()
                                : null
                )
                .rescheduledTo(
                        holiday.getRescheduledTo() != null
                                ? holiday.getRescheduledTo().toString()
                                : null
                )
                .build();
    }
}