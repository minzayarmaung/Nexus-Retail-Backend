package com.nexusretail.feature.merchant.controller;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.merchant.dto.request.CreateMerchantRequest;
import com.nexusretail.feature.merchant.dto.request.CreateShopRequest;
import com.nexusretail.feature.merchant.dto.request.UpdateShopRequest;
import com.nexusretail.feature.merchant.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/${api.base.path}/merchants")
@RequiredArgsConstructor
@Tag(name = "Shop Management", description = "Shop management operations")
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping("/owner")
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
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateMerchantRequest.class))
            )
            @RequestBody CreateMerchantRequest request, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = merchantService.createShopOwner(request);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Create Shop", description = "Create a new shop with addresses (SYSTEM_ADMIN only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shop created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or shop already exists",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - only SYSTEM_ADMIN allowed",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> createShop(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Shop creation data",
                required = true,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateShopRequest.class))
            )
            @RequestBody CreateShopRequest request, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = merchantService.createShop(request);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }

    @GetMapping("/shops")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Get All Shops", description = "Get all shops (SYSTEM_ADMIN sees all, OWNER sees only their shops)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shops retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> getAllShops(HttpServletRequest httpRequest) {
        ApiResponse apiResponse = merchantService.getAllShops();
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Get Shop by ID", description = "Get a specific shop by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shop retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Shop not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> getShopById(
            @Parameter(description = "Shop ID", required = true, example = "1")
            @PathVariable Long id, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = merchantService.getShopById(id);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Update Shop", description = "Update shop details (PATCH - only provided fields are updated)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shop updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Shop not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> updateShop(
            @Parameter(description = "Shop ID", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Shop update data (only provided fields will be updated)",
                required = true,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateShopRequest.class))
            )
            @RequestBody UpdateShopRequest request, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = merchantService.updateShop(id, request);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Delete Shop", description = "Delete a shop (SYSTEM_ADMIN only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shop deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied - only SYSTEM_ADMIN allowed",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Shop not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> deleteShop(
            @Parameter(description = "Shop ID", required = true, example = "1")
            @PathVariable Long id, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = merchantService.deleteShop(id);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Get Shops by Owner", description = "Get all shops owned by a specific owner")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shops retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> getShopsByOwner(
            @Parameter(description = "Owner ID", required = true, example = "1")
            @PathVariable Long ownerId, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = merchantService.getShopsByOwner(ownerId);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('OWNER')")
    @Operation(summary = "Search Shops", description = "Search shops by name")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shops search completed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse> searchShops(
            @Parameter(description = "Search term for shop name", example = "Nexus")
            @RequestParam(required = false) String searchTerm, HttpServletRequest httpRequest) {
        ApiResponse apiResponse = merchantService.searchShops(searchTerm);
        return ResponseUtils.buildResponse(httpRequest, apiResponse);
    }
}
