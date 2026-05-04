package com.nexusretail.feature.configuration.controller;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.configuration.dto.request.CodeValueRequest;
import com.nexusretail.feature.configuration.service.CodeValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.base.path}/configuration/code-values")
@RequiredArgsConstructor
@Tag(name = "Configuration - Code Values", description = "Code value management APIs for dropdown options")
public class CodeValueController {

    private final CodeValueService codeValueService;

    @PostMapping
    @Operation(summary = "Create Code Value", description = "Add a new value/option to a code type")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code value created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or duplicate value"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> createCodeValue(
            @RequestBody CodeValueRequest request,
            HttpServletRequest httpRequest) {
        ApiResponse response = codeValueService.createCodeValue(request);
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @GetMapping("/code/{codeId}")
    @Operation(summary = "Get Code Values by Code ID", description = "Retrieve all values/options for a specific code, ordered by position")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code values retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Code not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> getCodeValuesByCodeId(
            @PathVariable Long codeId,
            HttpServletRequest httpRequest) {
        ApiResponse response = codeValueService.getCodeValuesByCodeId(codeId);
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Code Value by ID", description = "Retrieve a specific code value by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code value retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Code value not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> getCodeValueById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        ApiResponse response = codeValueService.getCodeValueById(id);
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @GetMapping("/dropdown/{codeType}")
    @Operation(summary = "Get Dropdown Options", description = "Retrieve dropdown options for a specific code type (public API for UI)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dropdown options retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Code type not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> getDropdownOptions(
            @PathVariable String codeType,
            HttpServletRequest httpRequest) {
        ApiResponse response = codeValueService.getDropdownOptions(codeType);
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Code Value", description = "Update code value details")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code value updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or duplicate value"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Code value not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> updateCodeValue(
            @PathVariable Long id,
            @RequestBody CodeValueRequest request,
            HttpServletRequest httpRequest) {
        ApiResponse response = codeValueService.updateCodeValue(id, request);
        return ResponseUtils.buildResponse(httpRequest, response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Code Value", description = "Delete a code value/option")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Code value deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Code value not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> deleteCodeValue(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        ApiResponse response = codeValueService.deleteCodeValue(id);
        return ResponseUtils.buildResponse(httpRequest, response);
    }
}
