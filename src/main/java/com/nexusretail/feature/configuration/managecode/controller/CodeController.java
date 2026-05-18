package com.nexusretail.feature.configuration.managecode.controller;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.configuration.managecode.dto.request.CodeRequest;
import com.nexusretail.feature.configuration.managecode.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/configuration/codes")
@RequiredArgsConstructor
@Tag(name = "Configuration - Codes", description = "Code management APIs for dynamic dropdowns")
public class CodeController {

    private final CodeService codeService;

    @PreAuthorize("hasPermission(null, 'CREATE_CODE')")
    @PostMapping
    @Operation(summary = "Create Code", description = "Create a new code type for dynamic dropdown")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> createCode(
            @RequestBody CodeRequest request,
            HttpServletRequest httpRequest) {
        ApiResponse response = this.codeService.createCode(request);
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @PreAuthorize("hasPermission(null, 'READ_CODE')")
    @GetMapping
    @Operation(summary = "Get All Codes", description = "Retrieve all code types")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Codes retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> getAllCodes(HttpServletRequest httpRequest) {
        ApiResponse response = this.codeService.getAllCodes();
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @PreAuthorize("hasPermission(null, 'READ_CODE')")
    @GetMapping("/{id}")
    @Operation(summary = "Get Code by ID", description = "Retrieve code details by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Code not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> getCodeById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        ApiResponse response = this.codeService.getCodeById(id);
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @PreAuthorize("hasPermission(null, 'READ_CODE')")
    @GetMapping("/type/{codeType}")
    @Operation(summary = "Get Code by Type", description = "Retrieve code details by code type")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Code not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> getCodeByType(
            @PathVariable String codeType,
            HttpServletRequest httpRequest) {
        ApiResponse response = this.codeService.getCodeByType(codeType);
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @PreAuthorize("hasPermission(null, 'UPDATE_CODE')")
    @PutMapping("/{id}")
    @Operation(summary = "Update Code", description = "Update code details")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Code not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> updateCode(
            @PathVariable Long id,
            @RequestBody CodeRequest request,
            HttpServletRequest httpRequest) {
        ApiResponse response = this.codeService.updateCode(id, request);
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @PreAuthorize("hasPermission(null, 'DELETE_CODE')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Code", description = "Delete a code and all its values")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Code not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> deleteCode(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        final ApiResponse response = this.codeService.deleteCode(id);
        return ResponseUtils.buildResponse(httpRequest, response);
    }
}
