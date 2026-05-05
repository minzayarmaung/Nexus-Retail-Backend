package com.nexusretail.feature.employee.controller;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.employee.dto.request.EmployeeRequest;
import com.nexusretail.feature.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.base.path}/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "Employee CRUD operations with role-based access control")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Create Employee", description = "Create a new employee (OWNER only, within their shop)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Employee created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or employee already exists",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> createEmployee(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Employee creation data",
                required = true,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeRequest.class))
            )
            @RequestBody EmployeeRequest request, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = employeeService.createEmployee(request);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Get All Employees", description = "Get all employees with optional search and filter (OWNER: within shop, SYSTEM_ADMIN: all employees)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Employees retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> getAllEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String position,
            HttpServletRequest request) {
        ApiResponse apiResponse = employeeService.getAllEmployeesWithFilters(search, position);
        return ResponseUtils.buildResponse(request, apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Get Employee by ID", description = "Get employee by ID (OWNER: within shop, SYSTEM_ADMIN: any employee)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Employee retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Employee not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable Long id, HttpServletRequest request) {
        ApiResponse apiResponse = employeeService.getEmployeeById(id);
        return ResponseUtils.buildResponse(request, apiResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Update Employee", description = "Update employee (OWNER only, within their shop)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Employee updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Employee not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> updateEmployee(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Employee update data",
                required = true,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeRequest.class))
            )
            @RequestBody EmployeeRequest request, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = employeeService.updateEmployee(id, request);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Delete Employee", description = "Delete employee (OWNER only, within their shop)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Employee deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Employee not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Long id, HttpServletRequest request) {
        ApiResponse apiResponse = employeeService.deleteEmployee(id);
        return ResponseUtils.buildResponse(request, apiResponse);
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('OWNER') or hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Export Employees to Excel", description = "Export employees to Excel format (OWNER: within shop, SYSTEM_ADMIN: all employees)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Employee data ready for export",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> exportEmployees(HttpServletRequest request) {
        ApiResponse apiResponse = employeeService.exportEmployeesToExcel();
        return ResponseUtils.buildResponse(request, apiResponse);
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Import Employees from Excel", description = "Import employees from Excel file (OWNER only, within their shop)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Employee import completed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> importEmployees(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Excel file data as byte array",
                required = true,
                content = @Content(mediaType = "application/octet-stream")
            )
            @RequestBody byte[] excelData, HttpServletRequest request) {
        ApiResponse apiResponse = employeeService.importEmployeesFromExcel(excelData);
        return ResponseUtils.buildResponse(request, apiResponse);
    }
}
