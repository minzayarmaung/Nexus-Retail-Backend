package com.nexusretail.feature.user.controller;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.PaginatedApiResponse;
import com.nexusretail.feature.user.dto.response.UserResponse;
import com.nexusretail.feature.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.path}/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary     = "Get all users",
            description = "Returns a paginated list of all registered users."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Users retrieved successfully",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",          content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",             content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping
    public ResponseEntity<PaginatedApiResponse<UserResponse>> getAllUsers(
            @Parameter(description = "Zero-based page index (0..N)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Sort field", example = "username")
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        PaginatedApiResponse<UserResponse> response = userService.getAllUsers(pageable);
        return ResponseEntity.ok(response);
    }
}
