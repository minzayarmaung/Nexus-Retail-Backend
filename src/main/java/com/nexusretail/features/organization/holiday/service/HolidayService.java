package com.nexusretail.features.organization.holiday.service;

import com.nexusretail.features.organization.holiday.dto.HolidayData;
import com.nexusretail.features.organization.holiday.dto.request.HolidayDataRequest;

import java.time.LocalDate;
import java.util.Collection;

public interface HolidayService {
    Collection<HolidayData> retrieveAllHolidaysBySearchParameters(Long officeId, LocalDate fromDate, LocalDate toDate);

    String createNewHoliday(HolidayDataRequest holidayDataRequest);

    String activateHoliday(Long holidayId);

    HolidayData retrieveHoliday(Long holidayId);

    String updateHoliday(Long holidayId, HolidayDataRequest holidayDataRequest);

    String deleteHoliday(Long holidayId);
}
