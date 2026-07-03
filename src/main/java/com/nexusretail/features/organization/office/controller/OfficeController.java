package com.nexusretail.features.organization.office.controller;

import com.nexusretail.features.organization.office.dto.request.OfficeRequest;
import com.nexusretail.features.organization.office.service.OfficeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.path}/organization/office")
@RequiredArgsConstructor
@Tag(name = "Organization - Office", description = "Office management APIs for organization")
public class OfficeController {

    private final OfficeService officeService;

    @PreAuthorize("hasPermission(null, 'CREATE_OFFICE')")
    @PostMapping
    @Operation(summary = "Create an Office", description = "Mandatory Fields\n" + "name, openingDate, parentId")
    public String createOffice(@Parameter(hidden = true) final @RequestBody OfficeRequest officeRequest){
        return this.officeService.createOffice(officeRequest);
    }
}
