package com.nexusretail.features.organization.holiday.service.impl;

import com.nexusretail.common.constant.Status;
import com.nexusretail.data.models.Holiday;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.repositories.HolidayRespository;
import com.nexusretail.data.repositories.OfficeRepository;
import com.nexusretail.features.organization.holiday.dto.HolidayData;
import com.nexusretail.features.organization.holiday.dto.request.HolidayDataRequest;
import com.nexusretail.features.organization.holiday.service.HolidayService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
    @RequiredArgsConstructor
    public class HolidayServiceImpl implements HolidayService {

        private final HolidayRespository holidayRepository;
        private final OfficeRepository officeRepository;

        @Override
        public String createNewHoliday(HolidayDataRequest holidayDataRequest) {

            validateHolidayRequest(holidayDataRequest);

            Holiday holiday = new Holiday();

            holiday.setName(holidayDataRequest.name());
            holiday.setFromDate(holidayDataRequest.fromDate());
            holiday.setToDate(holidayDataRequest.toDate());
            holiday.setRescheduledTo(holidayDataRequest.rescheduledTo());
            holiday.setDescription(holidayDataRequest.description());

            holiday.setOffices(
                    holidayDataRequest.officeId() == null
                            ? Set.of()
                            : holidayDataRequest.officeId()
            );

            holiday.setProcessed(false);
            holiday.setStatus(Status.INACTIVE);

            holidayRepository.save(holiday);

            return "Holiday created successfully";
        }

    @Override
    public String activateHoliday(Long holidayId) {
            Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found with id: " + holidayId));

            holiday.setStatus(Status.ACTIVE);
            holidayRepository.save(holiday);

            return "Holiday Activated Successfully.";
    }

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

        private void validateHolidayRequest(HolidayDataRequest request) {

            if (request.name() == null || request.name().isBlank()) {
                throw new IllegalArgumentException("Holiday name is required");
            }

            if (request.fromDate() == null) {
                throw new IllegalArgumentException("From date is required");
            }

            if (request.toDate() == null) {
                throw new IllegalArgumentException("To date is required");
            }

            if (request.fromDate().isAfter(request.toDate())) {
                throw new IllegalArgumentException(
                        "From date must not be after to date"
                );
            }

            if (request.rescheduledTo() != null
                    && request.rescheduledTo().isBefore(request.toDate())) {
                throw new IllegalArgumentException(
                        "Rescheduled date must be after holiday end date"
                );
            }
        }
}