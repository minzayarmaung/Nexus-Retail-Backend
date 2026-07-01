package com.nexusretail.features.organization.holiday.controller;

import com.nexusretail.features.organization.holiday.dto.HolidayData;
import com.nexusretail.features.organization.holiday.dto.request.HolidayDataRequest;
import com.nexusretail.features.organization.holiday.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("${api.base.path}/organization/holiday")
@RequiredArgsConstructor
@Tag(name = "Organization - Holiday", description = "Holiday management APIs for organization")
public class HolidayController {

    private HolidayService holidayService;

    @PreAuthorize("hasPermission(null, 'READ_HOLIDAY')")
    @GetMapping
    @Operation(summary = "Retrieve all holidays", description = "Retrieve all holidays for the organization")
    public Collection<HolidayData> retrieveAllHolidays(
            @RequestParam(required = false) Long officeId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate){

        return this.holidayService.retrieveAllHolidaysBySearchParameters(officeId , fromDate , toDate);
    }

    @PreAuthorize("hasPermission(null, 'CREATE_HOLIDAY')")
    @PostMapping
    @Operation(summary = "Create a new holiday", description = "Create a new holiday for the organization")
    public String createNewHoliday(@Parameter(hidden = true) final @RequestBody HolidayDataRequest holidayDataRequest) {
        return this.holidayService.createNewHoliday(holidayDataRequest);
    }
}
