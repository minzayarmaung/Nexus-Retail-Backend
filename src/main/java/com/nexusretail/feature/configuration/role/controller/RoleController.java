package com.nexusretail.feature.configuration.role.controller;

import com.nexusretail.feature.configuration.role.dto.response.RoleResponse;
import com.nexusretail.feature.configuration.role.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/roles")
@Tag(name = "Roles Controller", description = "Manage Roles and Permissions")
public class RoleController {

    private final RoleService roleService;

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

    
}
