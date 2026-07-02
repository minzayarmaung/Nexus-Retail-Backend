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

    @PreAuthorize("hasPermission(null, 'ACTIVATE_HOLIDAY')")
    @PatchMapping("/{holidayId}/activate")
    @Operation(summary = "Activate a holiday", description = "Activate a holiday for the organization")
    public String activateHoliday(@PathParam("holidayId") final Long holidayId){
        return this.holidayService.activateHoliday(holidayId);
    }

    @PreAuthorize("hasPermission(null, 'READ_HOLIDAY')")
    @GetMapping("/{holidayId}")
    @Operation(summary = "Retrieve a specific holiday", description = "Retrieve a specific holiday for the organization")
    public HolidayData retrieveOne(@Parameter(hidden = true) final @PathVariable Long holidayId) {
        return this.holidayService.retrieveHoliday(holidayId);
    }

    @PreAuthorize("hasPermission(null, 'UPDATE_HOLIDAY')")
    @PutMapping("/{holidayId}")
    @Operation(summary = "Update a holiday", description = "Update a holiday for the organization")
    public String updateHoliday(final @PathVariable Long holidayId, @Parameter(hidden = true) final @RequestBody HolidayDataRequest holidayDataRequest) {
        return this.holidayService.updateHoliday(holidayId , holidayDataRequest);
    }

    @PreAuthorize("hasPermission(null, 'DELETE_HOLIDAY')")
    @DeleteMapping("/{holidayId}")
    @Operation(summary = "Delete a holiday", description = "Delete a holiday for the organization")
    public String deleteHoliday(final @PathVariable Long holidayId) {
        return this.holidayService.deleteHoliday(holidayId);
    }
}
