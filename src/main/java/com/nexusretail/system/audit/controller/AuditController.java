package com.nexusretail.system.audit.controller;


import com.nexusretail.common.audit.AuditSearchCriteria;
import com.nexusretail.common.dto.AuditLogDto;
import com.nexusretail.data.models.AuditLog;
import com.nexusretail.system.audit.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("${api.base.path}/system/audit")
@RequiredArgsConstructor
@Tag(name = "Audit Management", description = "Audit management APIs")
public class AuditController {

    private final AuditService auditService;

    @PreAuthorize("hasPermission(null, 'READ_AUDIT_LOG')")
    @GetMapping
    @Operation(
            summary = "Search Audit Logs",
            description = """
        Retrieve paginated audit logs with optional filters.
        All filter params are optional and combinable.
        Basic mode: use action, makerName, processingResult, from/to.
        Advanced mode: additionally use actionMethod, browserName, deviceModel,
        operatingSystem, operatingSystemVersion.
        """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Audit logs retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AuditLogDto>> getAuditLogs(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityName,
            @RequestParam(required = false) Long   entityId,
            @RequestParam(required = false) String makerName,
            @RequestParam(required = false) String processingResult,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) String actionMethod,
            @RequestParam(required = false) String browserName,
            @RequestParam(required = false) String deviceModel,
            @RequestParam(required = false) String operatingSystem,
            @RequestParam(required = false) String operatingSystemVersion,
            @PageableDefault(
                    size = 20,
                    sort = "madeOnDate",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        AuditSearchCriteria criteria = new AuditSearchCriteria();
        criteria.setAction(action);
        criteria.setEntityName(entityName);
        criteria.setEntityId(entityId);
        criteria.setMakerName(makerName);
        criteria.setProcessingResult(processingResult);
        criteria.setFrom(from);
        criteria.setTo(to);
        criteria.setActionMethod(actionMethod);
        criteria.setBrowserName(browserName);
        criteria.setDeviceModel(deviceModel);
        criteria.setOperatingSystem(operatingSystem);
        criteria.setOperatingSystemVersion(operatingSystemVersion);

        return ResponseEntity.ok(auditService.searchAuditLogs(criteria, pageable));
    }
}
