package com.nexusretail.feature.employee.service;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.employee.dto.request.EmployeeRequest;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {

    /**
     * Create a new employee
     * - OWNER can create employees only in their shop
     * - Optionally create user account with specified role
     */
    ApiResponse createEmployee(EmployeeRequest request);

    /**
     * Get all employees
     * - SYSTEM_ADMIN: can view all employees (read-only)
     * - OWNER: can view only employees in their shop
     */
    ApiResponse getAllEmployees();

    /**
     * Get employee by ID
     * - SYSTEM_ADMIN: can view any employee
     * - OWNER: can view only employees in their shop
     */
    ApiResponse getEmployeeById(Long id);

    /**
     * Update employee
     * - OWNER can update employees only in their shop
     * - SYSTEM_ADMIN cannot update employees
     */
    ApiResponse updateEmployee(Long id, EmployeeRequest request);

    /**
     * Delete employee
     * - OWNER can delete employees only in their shop
     * - SYSTEM_ADMIN cannot delete employees
     */
    ApiResponse deleteEmployee(Long id);

    /**
     * Get all employees with search and filter options
     * - SYSTEM_ADMIN: can view all employees (read-only)
     * - OWNER: can view only employees in their shop
     */
    ApiResponse getAllEmployeesWithFilters(String searchTerm, String position);

    /**
     * Export employees to Excel
     * - SYSTEM_ADMIN: can export all employees
     * - OWNER: can export only employees in their shop
     */
    ApiResponse exportEmployeesToExcel();

    /**
     * Import employees from Excel
     * - OWNER can import employees only in their shop
     * - SYSTEM_ADMIN cannot import employees
     */
    ApiResponse importEmployeesFromExcel(byte[] excelData);
}
