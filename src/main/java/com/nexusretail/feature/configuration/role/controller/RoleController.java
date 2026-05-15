package com.nexusretail.feature.configuration.role.controller;

import com.nexusretail.feature.configuration.role.dto.request.RoleRequest;
import com.nexusretail.feature.configuration.role.dto.response.RoleResponse;
import com.nexusretail.feature.configuration.role.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

    @PreAuthorize("hasPermission('READ_ROLE_PERMISSION')")
    @GetMapping
    @Operation(summary = "List Roles", description = "Example Requests:\n" + "\n" + "roles\n" + "\n" + "\n" + "roles?fields=name")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Collection<RoleResponse>> retrieveAllRoles(HttpServletRequest request){
        final Collection<RoleResponse> roles = roleService.retrieveAllRoles(request);
        return ResponseEntity.ok().body(roles);
    }

    @PreAuthorize("hasPermission('READ_ROLE')")
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
    public ResponseEntity<RoleResponse> retrieveRoleById(@PathVariable Long id, HttpServletRequest request){
        final RoleResponse role = this.roleService.retrieveRoleById(id, request);
        return ResponseEntity.ok().body(role);
    }

    @PreAuthorize("hasPermission('CREATE_ROLE')")
    @PostMapping
    @Operation(summary = "Create Role", description = "Example Requests:\n" + "\n" + "roles\n" + "\n" + "\n" + "roles?fields=name")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Role created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest roleRequest){
        final RoleResponse response = this.roleService.createRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasPermission('UPDATE_ROLE_PERMISSION')")
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
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @RequestBody RoleRequest roleRequest
    ) {
        final RoleResponse response = roleService.updateRole(id, roleRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission('DELETE_ROLE')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Role", description = "Delete Role by Id")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        this.roleService.deleteRole(id);
        return ResponseEntity.ok("Role with id " + id + " has been deleted successfully.");
    }
}
