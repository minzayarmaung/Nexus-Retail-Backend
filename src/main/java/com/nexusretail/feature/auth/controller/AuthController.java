package com.nexusretail.feature.auth.controller;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.auth.dto.request.LoginRequest;
import com.nexusretail.feature.auth.service.AuthService;
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
@RequestMapping("/${api.base.path}/auth")
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
            @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        ApiResponse response = authService.loginUser(loginRequest, request);
        return ResponseUtils.buildResponse(request, response);
    }
}
