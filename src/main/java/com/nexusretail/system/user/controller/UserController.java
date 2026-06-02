package com.nexusretail.system.user.controller;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.system.user.dto.request.UserCreateRequest;
import com.nexusretail.system.user.dto.request.UserUpdateRequest;
import com.nexusretail.system.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/system/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasPermission(null, 'GET_USER')")
    @GetMapping
    @Operation(summary = "Get Users", description = "Retrieve a list of all users")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> getUsers(){
        final ApiResponse response = this.userService.getUsers();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission(null, 'CREATE_USER')")
    @PostMapping
    @Operation(summary = "Create User ", description = "Create a new user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateRequest userCreateRequest){
        final ApiResponse response = this.userService.createUser(userCreateRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission(null, 'UPDATE_USER')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update User ", description = "Update an existing user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest , @PathVariable Long id){
        final ApiResponse response = this.userService.updateUser(userUpdateRequest , id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission(null, 'CREATE_USER')")
    @GetMapping("/check-username")
    @Operation(summary = "Check Username Availability", description = "Check if a username is available for registration")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Username already exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(this.userService.checkUsername(username));
    }

    @PreAuthorize("hasPermission(null, 'CREATE_USER')")
    @GetMapping("generate-password")
    @Operation(summary = "Generate Password", description = "Generate a random password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password generated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String generatePassword(@RequestParam String username) {
        return this.userService.generatePassword(username);
    }

    @PreAuthorize("hasPermission(null, 'SUSPEND_USER')")
    @PostMapping("/suspend/{id}")
    @Operation(summary = "Suspend User", description = "Suspend a user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User suspended successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String suspendUser(@PathVariable Long id) {
        return this.userService.suspendUser(id);
    }

    @PreAuthorize("hasPermission(null, 'DELETE_USER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User", description = "Delete a user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String deleteUser(@PathVariable Long id) {
        return this.userService.deleteUser(id);
    }

}
