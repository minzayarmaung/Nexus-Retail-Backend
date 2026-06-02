package com.nexusretail.system.auth.controller;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.system.auth.dto.request.LoginRequest;
import com.nexusretail.system.auth.service.AuthService;
import com.nexusretail.system.auth.dto.request.ResetPasswordRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.base.path}/system/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user with username or email and password")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Login credentials",
                required = true,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class))
            )
            @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        ApiResponse apiResponse = authService.loginUser(loginRequest, request, response);
        return ResponseUtils.buildResponse(request, apiResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Logout user by clearing JWT token cookie")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout successful",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        ApiResponse apiResponse = authService.logoutUser(request, response);
        return ResponseUtils.buildResponse(request, apiResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(HttpServletRequest request,
                                                    HttpServletResponse httpResponse) {
        final ApiResponse response = authService.refreshToken(request, httpResponse);
        return ResponseUtils.buildResponse(request , response);
    }

    @PreAuthorize("hasPermission(null, 'UPDATE_USER')")
    @PatchMapping("/change-password/{id}")
    @Operation(summary = "Change Password", description = "Change User Password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password Updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")    })
    public ResponseEntity<ApiResponse> changePassword(@PathVariable Long id ,@RequestParam String newPassword , HttpServletRequest request) {
        final ApiResponse response = this.authService.changePassword(id , newPassword);
        return ResponseUtils.buildResponse(request , response);
    }

    @PreAuthorize("hasPermission(null, 'UPDATE_USER')")
    @PatchMapping
    @Operation(summary = "Reset Password" , description = "Reset User Password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200" , description = "Password Reset Successfully.")
    })
    public ResponseEntity<ApiResponse> resetPassword(@PathVariable Long id , @RequestBody ResetPasswordRequest resetPasswordRequest, HttpServletRequest request){
        final ApiResponse response = this.authService.resetPassword(id , resetPasswordRequest);
        return ResponseUtils.buildResponse(request , response);
    }
}
