package com.nexusretail.features.organization.holiday.service;

import com.nexusretail.features.organization.holiday.dto.HolidayData;

import java.time.LocalDate;
import java.util.Collection;

public interface HolidayService {
    Collection<HolidayData> retrieveAllHolidaysBySearchParameters(Long officeId, LocalDate fromDate, LocalDate toDate);
}
