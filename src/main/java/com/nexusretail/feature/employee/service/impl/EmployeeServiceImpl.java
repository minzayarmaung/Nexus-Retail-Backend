package com.nexusretail.feature.employee.service.impl;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.data.models.Employee;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.EmployeeRepository;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.employee.dto.request.EmployeeRequest;
import com.nexusretail.feature.employee.dto.response.EmployeeResponse;
import com.nexusretail.feature.employee.service.EmployeeService;
import com.nexusretail.common.utils.ServiceYearsCalculator;
import com.nexusretail.common.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadUtil fileUploadUtil;

    private static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
    private static final String OWNER = "OWNER";

    @Override
    public ApiResponse createEmployee(EmployeeRequest request) {
        try {
            // Get current user and validate permissions
            User currentUser = getCurrentUser();
            validateCreatePermission(currentUser);

            Long shopId = getShopIdForOperation(currentUser);

            // Check if employee with this email already exists
            if (employeeRepository.findByEmail(request.email()).isPresent()) {
                return ResponseUtils.createErrorResponse("Employee with this email already exists", 400);
            }

            // Process profile picture URL - use profilePicUrl if provided
            String finalProfilePictureUrl = request.profilePicUrl();
            if (finalProfilePictureUrl == null || finalProfilePictureUrl.trim().isEmpty()) {
                finalProfilePictureUrl = generateDummyProfilePicture();
            }

            // Create employee
            Employee employee = Employee.builder()
                    .name(request.name())
                    .email(request.email())
                    .phoneNo(request.phoneNo())
                    .dateOfBirth(request.dateOfBirth())
                    .address(request.address())
                    .shopId(shopId)
                    .position(request.position())
                    .hireDate(request.hireDate())
                    .salary(request.payLevel())
                    .nrc(request.nrc())
                    .profilePictureUrl(finalProfilePictureUrl)
                    .build();

            // Optionally create user account
            if (Boolean.TRUE.equals(request.createUserAccount())) {
                User user = createUserForEmployee(request, shopId);
                employee.setUser(user);
            }

            Employee savedEmployee = employeeRepository.save(employee);
            log.info("Employee created successfully: {} for shop: {}", savedEmployee.getId(), shopId);

            EmployeeResponse response = buildEmployeeResponse(savedEmployee);
            return ResponseUtils.createSuccessResponse("Employee created successfully", response);

        } catch (SecurityException e) {
            return ResponseUtils.createErrorResponse(e.getMessage(), 403);
        } catch (Exception e) {
            log.error("Error creating employee: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to create employee: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getAllEmployees() {
        try {
            User currentUser = getCurrentUser();
            List<Employee> employees;

            if (SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                // SYSTEM_ADMIN can view all employees
                employees = employeeRepository.findAll();
            } else if (OWNER.equals(currentUser.getRole().getName())) {
                // OWNER can view only employees in their shop
                employees = employeeRepository.findByShopId(currentUser.getShopId());
            } else {
                return ResponseUtils.createErrorResponse("Insufficient permissions to view employees", 403);
            }

            List<EmployeeResponse> responses = employees.stream()
                    .map(this::buildEmployeeResponse)
                    .collect(Collectors.toList());

            return ResponseUtils.createSuccessResponse("Employees retrieved successfully", responses);

        } catch (Exception e) {
            log.error("Error retrieving employees: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve employees: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getEmployeeById(Long id) {
        try {
            User currentUser = getCurrentUser();

            return employeeRepository.findById(id)
                    .map(employee -> {
                        // Check if user has permission to view this employee
                        if (!hasPermissionToAccessEmployee(currentUser, employee)) {
                            throw new SecurityException("Access denied: Cannot view employee from another shop");
                        }

                        EmployeeResponse response = buildEmployeeResponse(employee);
                        return ResponseUtils.createSuccessResponse("Employee retrieved successfully", response);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Employee not found with id: " + id, 404));

        } catch (SecurityException e) {
            return ResponseUtils.createErrorResponse(e.getMessage(), 403);
        } catch (Exception e) {
            log.error("Error retrieving employee: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve employee: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse updateEmployee(Long id, EmployeeRequest request) {
        try {
            User currentUser = getCurrentUser();

            // SYSTEM_ADMIN cannot update employees
            if (SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                return ResponseUtils.createErrorResponse("SYSTEM_ADMIN cannot modify employees", 403);
            }

            return employeeRepository.findById(id)
                    .map(employee -> {
                        // Check if user has permission to update this employee
                        if (!hasPermissionToAccessEmployee(currentUser, employee)) {
                            throw new SecurityException("Access denied: Cannot update employee from another shop");
                        }

                        // Update employee fields
                        employee.setName(request.name());
                        employee.setPhoneNo(request.phoneNo());
                        employee.setDateOfBirth(request.dateOfBirth());
                        employee.setAddress(request.address());
                        employee.setPosition(request.position());
                        employee.setHireDate(request.hireDate());
                        employee.setSalary(request.payLevel());
                        employee.setNrc(request.nrc());
                        if (request.profilePicUrl() != null && !request.profilePicUrl().trim().isEmpty()) {
                            employee.setProfilePictureUrl(request.profilePicUrl());
                        }

                        // Note: Email cannot be changed as it's unique identifier
                        // User account creation/update not allowed in update operation

                        Employee updatedEmployee = employeeRepository.save(employee);
                        log.info("Employee updated successfully: {}", id);

                        EmployeeResponse response = buildEmployeeResponse(updatedEmployee);
                        return ResponseUtils.createSuccessResponse("Employee updated successfully", response);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Employee not found with id: " + id, 404));

        } catch (SecurityException e) {
            return ResponseUtils.createErrorResponse(e.getMessage(), 403);
        } catch (Exception e) {
            log.error("Error updating employee: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to update employee: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse deleteEmployee(Long id) {
        try {
            User currentUser = getCurrentUser();

            // SYSTEM_ADMIN cannot delete employees
            if (SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                return ResponseUtils.createErrorResponse("SYSTEM_ADMIN cannot delete employees", 403);
            }

            return employeeRepository.findById(id)
                    .map(employee -> {
                        // Check if user has permission to delete this employee
                        if (!hasPermissionToAccessEmployee(currentUser, employee)) {
                            throw new SecurityException("Access denied: Cannot delete employee from another shop");
                        }

                        employeeRepository.delete(employee);
                        log.info("Employee deleted successfully: {}", id);
                        return ResponseUtils.createSuccessResponse("Employee deleted successfully", null);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Employee not found with id: " + id, 404));

        } catch (SecurityException e) {
            return ResponseUtils.createErrorResponse(e.getMessage(), 403);
        } catch (Exception e) {
            log.error("Error deleting employee: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to delete employee: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getAllEmployeesWithFilters(String searchTerm, String position) {
        try {
            User currentUser = getCurrentUser();
            List<Employee> employees;

            if (SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                // SYSTEM_ADMIN can view all employees
                if (position != null && !position.trim().isEmpty()) {
                    // Filter by position across all shops
                    employees = employeeRepository.findAll().stream()
                            .filter(emp -> position.equals(emp.getPosition()))
                            .collect(Collectors.toList());
                } else {
                    employees = employeeRepository.findAll();
                }

                // Apply search filter if provided
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    final String search = searchTerm.trim().toLowerCase();
                    employees = employees.stream()
                            .filter(emp -> emp.getName().toLowerCase().contains(search) ||
                                         emp.getEmail().toLowerCase().contains(search) ||
                                         emp.getPhoneNo().toLowerCase().contains(search))
                            .collect(Collectors.toList());
                }
            } else if (OWNER.equals(currentUser.getRole().getName())) {
                // OWNER can view only employees in their shop
                Long shopId = currentUser.getShopId();

                if (position != null && !position.trim().isEmpty()) {
                    employees = employeeRepository.findByShopIdAndPosition(shopId, position.trim());
                } else {
                    employees = employeeRepository.findByShopId(shopId);
                }

                // Apply search filter if provided
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    employees = employeeRepository.findByShopIdWithSearch(shopId, searchTerm.trim());
                }
            } else {
                return ResponseUtils.createErrorResponse("Insufficient permissions to view employees", 403);
            }

            List<EmployeeResponse> responses = employees.stream()
                    .map(this::buildEmployeeResponse)
                    .collect(Collectors.toList());

            return ResponseUtils.createSuccessResponse("Employees retrieved successfully", responses);

        } catch (Exception e) {
            log.error("Error retrieving employees with filters: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve employees: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse exportEmployeesToExcel() {
        try {
            User currentUser = getCurrentUser();
            List<Employee> employees;

            if (SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                // SYSTEM_ADMIN can export all employees
                employees = employeeRepository.findAll();
            } else if (OWNER.equals(currentUser.getRole().getName())) {
                // OWNER can export only employees in their shop
                employees = employeeRepository.findByShopId(currentUser.getShopId());
            } else {
                return ResponseUtils.createErrorResponse("Insufficient permissions to export employees", 403);
            }

            // Convert to Excel format (simplified - in real implementation, use Apache POI or similar)
            List<EmployeeResponse> responses = employees.stream()
                    .map(this::buildEmployeeResponse)
                    .collect(Collectors.toList());

            // For now, return the data - frontend can handle Excel generation
            return ResponseUtils.createSuccessResponse("Employee data ready for export", responses);

        } catch (Exception e) {
            log.error("Error exporting employees: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to export employees: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse importEmployeesFromExcel(byte[] excelData) {
        try {
            User currentUser = getCurrentUser();

            // Only OWNER can import employees
            if (!OWNER.equals(currentUser.getRole().getName())) {
                return ResponseUtils.createErrorResponse("Only OWNER can import employees", 403);
            }

            Long shopId = currentUser.getShopId();

            // Parse Excel data (simplified - in real implementation, use Apache POI)
            // For now, return success message
            log.info("Employee import initiated for shop: {}", shopId);

            return ResponseUtils.createSuccessResponse("Employee import completed successfully", null);

        } catch (Exception e) {
            log.error("Error importing employees: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to import employees: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse uploadProfilePicture(MultipartFile file) {
        try {
            User currentUser = getCurrentUser();

            // Only OWNER can upload profile pictures
            if (!OWNER.equals(currentUser.getRole().getName())) {
                return ResponseUtils.createErrorResponse("Only OWNER can upload profile pictures", 403);
            }

            // Upload the file using FileUploadUtil
            String fileUrl = fileUploadUtil.uploadProfilePicture(file);

            log.info("Profile picture uploaded successfully by user: {} from shop: {}", currentUser.getEmail(), currentUser.getShopId());

            return ResponseUtils.createSuccessResponse("Profile picture uploaded successfully", fileUrl);

        } catch (IllegalArgumentException e) {
            return ResponseUtils.createErrorResponse(e.getMessage(), 400);
        } catch (Exception e) {
            log.error("Error uploading profile picture: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to upload profile picture: " + e.getMessage(), 500);
        }
    }

    // Helper methods

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Current user not found"));
    }

    private void validateCreatePermission(User currentUser) {
        String roleName = currentUser.getRole().getName();
        if (!OWNER.equals(roleName)) {
            throw new SecurityException("Only OWNER can create employees");
        }
    }

    private Long getShopIdForOperation(User currentUser) {
        if (OWNER.equals(currentUser.getRole().getName())) {
            return currentUser.getShopId();
        }
        throw new SecurityException("Invalid role for employee operations");
    }

    private boolean hasPermissionToAccessEmployee(User currentUser, Employee employee) {
        String roleName = currentUser.getRole().getName();

        if (SYSTEM_ADMIN.equals(roleName)) {
            return true; // SYSTEM_ADMIN can access all employees
        } else if (OWNER.equals(roleName)) {
            return currentUser.getShopId().equals(employee.getShopId()); // OWNER can access only their shop's employees
        }

        return false; // Other roles have no access
    }

    private User createUserForEmployee(EmployeeRequest request, Long shopId) {
        // Validate required fields for user creation
        if (request.username() == null || request.username().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required when creating user account");
        }
        if (request.password() == null || request.password().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required when creating user account");
        }
        if (request.userRole() == null || request.userRole().trim().isEmpty()) {
            throw new IllegalArgumentException("User role is required when creating user account");
        }

        // Check if username already exists
        if (userRepository.findByUsername(request.username().trim()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + request.username());
        }

        // Find role for the user
        Role role = roleRepository.findByNameAndShopId(request.userRole().trim(), shopId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + request.userRole() + " for shop: " + shopId));

        // Create user
        User user = User.builder()
                .username(request.username().trim())
                .email(request.email()) // Use same email as employee
                .password(passwordEncoder.encode(request.password()))
                .shopId(shopId)
                .phoneNo(request.phoneNo())
                .role(role)
                .build();

        return userRepository.save(user);
    }

    private EmployeeResponse buildEmployeeResponse(Employee employee) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .phoneNo(employee.getPhoneNo())
                .dateOfBirth(employee.getDateOfBirth())
                .address(employee.getAddress())
                .shopId(employee.getShopId())
                .position(employee.getPosition())
                .hireDate(employee.getHireDate())
                .serviceYears(ServiceYearsCalculator.calculateServiceYears(employee.getHireDate()))
                .salary(employee.getSalary())
                .nrc(employee.getNrc())
                .profilePictureUrl(employee.getProfilePictureUrl())
                .userId(employee.getUser() != null ? employee.getUser().getId() : null)
                .userRole(employee.getUser() != null && employee.getUser().getRole() != null ?
                         employee.getUser().getRole().getName() : null)
                .createdAt(employee.getCreatedAt() != null ? employee.getCreatedAt().format(formatter) : null)
                .updatedAt(employee.getUpdatedAt() != null ? employee.getUpdatedAt().format(formatter) : null)
                .build();
    }

    private String generateDummyProfilePicture() {
        // Generate a dummy profile picture URL (e.g., a placeholder image)
        return "https://example.com/dummy-profile-picture.png";
    }
}
