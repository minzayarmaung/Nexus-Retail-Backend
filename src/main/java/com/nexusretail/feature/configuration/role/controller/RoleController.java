package com.nexusretail.feature.configuration.role.controller;

import com.nexusretail.feature.configuration.role.dto.request.RoleRequest;
import com.nexusretail.feature.configuration.role.dto.request.UpdateRolePermissionsRequest;
import com.nexusretail.feature.configuration.role.dto.response.RolePermissionResponse;
import com.nexusretail.feature.configuration.role.dto.response.RolePermissionUpdateResponse;
import com.nexusretail.feature.configuration.role.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/roles")
@Tag(name = "Role Permissions", description = "Assign or remove permissions from a role")
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasPermission(null, 'READ_ROLE_PERMISSION')")
    @GetMapping
    @Operation(summary = "List Roles", description = "Example Requests:\n" + "\n" + "roles\n" + "\n" + "\n" + "roles?fields=name")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Collection<RolePermissionResponse>> retrieveAllRoles(HttpServletRequest request){
        final Collection<RolePermissionResponse> roles = roleService.retrieveAllRoles(request);
        return ResponseEntity.ok().body(roles);
    }

    @PreAuthorize("hasPermission(null, 'READ_ROLE')")
    @GetMapping("/{id}")
    @Operation(summary = "Get Role by ID", description = "Example Requests:\n" + "\n" + "roles/1\n" + "\n" + "\n" + "roles/1?fields=name")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role Not Found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<RolePermissionResponse> retrieveRoleById(@PathVariable Long id, HttpServletRequest request){
        final RolePermissionResponse role = this.roleService.retrieveRoleById(id, request);
        return ResponseEntity.ok().body(role);
    }

    @PreAuthorize("hasPermission(null, 'READ_ROLE')")
    @GetMapping("/{id}/permissions")
    @Operation(summary = "Get Role Permissions by ID", description = "Example Requests:\n" + "\n" + "roles/1/permissions\n" + "\n" + "\n" + "roles/1?fields=name")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role Permissions retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role Not Found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<RolePermissionResponse> retrieveRolePermissionByRoleId(@PathVariable Long id, HttpServletRequest request){
        final RolePermissionResponse role = this.roleService.retrieveRolePermissionsByRoleId(id, request);
        return ResponseEntity.ok().body(role);
    }

    @PreAuthorize("hasPermission(null, 'CREATE_ROLE')")
    @PostMapping
    @Operation(summary = "Create Role", description = "Example Requests:\n" + "\n" + "roles\n" + "\n" + "\n" + "roles?fields=name")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Role created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<RolePermissionResponse> createRole(@RequestBody RoleRequest roleRequest){
        final RolePermissionResponse response = this.roleService.createRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasPermission(null ,'UPDATE_ROLE_PERMISSION')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update Role", description = "Example Requests:\n" + "\n" + "roles/1\n" + "\n" + "\n" + "roles/1?fields=name")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode   = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role Not Found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<RolePermissionResponse> updateRole(@PathVariable Long id, @RequestBody RoleRequest roleRequest
    ) {
        final RolePermissionResponse response = roleService.updateRole(id, roleRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission(null ,'UPDATE_ROLE_PERMISSION')")
    @PatchMapping("/{id}/permissions")
    @Operation(summary = "Update Role Permissions", description = "Example Requests:\n" + "\n" + "roles/1\n" + "\n" + "\n" + "roles/1/permissions")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role Permissions updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode   = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role Not Found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<RolePermissionUpdateResponse> updateRolePermissions(
            @PathVariable Long id,
            @RequestBody @Valid UpdateRolePermissionsRequest request) {

        final RolePermissionUpdateResponse response = this.roleService.updateRolePermissions(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission(null, 'DELETE_ROLE')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Role", description = "Delete Role by Id")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        this.roleService.deleteRole(id);
        return ResponseEntity.ok("Role with id " + id + " has been deleted successfully.");
    }
}
