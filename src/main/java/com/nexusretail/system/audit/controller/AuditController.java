package com.nexusretail.system.audit.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.base.path}/audit")
@RequiredArgsConstructor
@Tag(name = "Audit Management", description = "Audit management APIs")
public class AuditController {

    @GetMapping
    @Operation(summary = "Get Audit Logs", description = "Retrieve audit logs for system activities")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String getAuditLog(){
        return "";
    }
}
