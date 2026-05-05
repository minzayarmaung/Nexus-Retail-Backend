package com.nexusretail.feature.shop.controller;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.shop.dto.request.CreateShopOwnerRequest;
import com.nexusretail.feature.shop.service.ShopOwnerService;
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
@RequestMapping("/${api.base.path}/shops")
@RequiredArgsConstructor
@Tag(name = "Shop Management", description = "Shop management operations")
public class ShopController {

    private final ShopOwnerService shopOwnerService;

    @PostMapping("/owners")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Create Shop Owner", description = "Create a new shop owner user (SYSTEM_ADMIN only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shop owner created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or user already exists",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - only SYSTEM_ADMIN allowed",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> createShopOwner(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Shop owner creation data",
                required = true,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateShopOwnerRequest.class))
            )
            @RequestBody CreateShopOwnerRequest request, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = shopOwnerService.createShopOwner(request);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }
}
