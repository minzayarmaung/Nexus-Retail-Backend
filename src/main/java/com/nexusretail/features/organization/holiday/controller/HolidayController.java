package com.nexusretail.features.organization.holiday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("${api.base.path}/organization/holiday")
@RequiredArgsConstructor
@Tag(name = "Organization - Holiday", description = "Holiday management APIs for organization")
public class HolidayController {

    @PreAuthorize("hasPermission(null, 'READ_HOLIDAY')")
    @GetMapping
    @Operation(summary = "Retrieve all holidays", description = "Retrieve all holidays for the organization")
    public String retrieveAllHolidays(
            @PathParam("officeId") @Parameter(description = "officeId") final Long officeId,
            @PathParam("fromDate") @Parameter(description = "fromDate") final LocalDate fromDateParam,
            @PathParam("toDate") @Parameter(description = "toDate") final LocalDate toDateParam,
            @PathParam("locale") @Parameter(description = "locale") final String locale,
            @PathParam("dateFormat") @Parameter(description = "dateFormat") final String rawDateFormat
    ){
        return "Retrieve all holidays for the organization";
    }
}
