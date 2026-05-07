package com.nexusretail.feature.merchant.service;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.merchant.dto.request.CreateMerchantRequest;
import com.nexusretail.feature.merchant.dto.request.CreateShopRequest;
import com.nexusretail.feature.merchant.dto.request.UpdateShopRequest;
import org.springframework.stereotype.Service;

@Service
public interface MerchantService {

    /**
     * Create a new shop owner (SYSTEM_ADMIN only)
     * - Creates a user with OWNER role
     * - Assigns the specified shopId
     */
    ApiResponse createShopOwner(CreateMerchantRequest request);

    /**
     * Create a new shop (SYSTEM_ADMIN only)
     * - Creates shop with addresses
     * - Assigns owner to the shop
     */
    ApiResponse createShop(CreateShopRequest request);

    /**
     * Get all shops (SYSTEM_ADMIN sees all, OWNER sees only their shop)
     */
    ApiResponse getAllShops();

    /**
     * Get shop by ID
     */
    ApiResponse getShopById(Long id);

    /**
     * Update shop (PATCH operation for better performance)
     */
    ApiResponse updateShop(Long id, UpdateShopRequest request);

    /**
     * Delete shop
     */
    ApiResponse deleteShop(Long id);

    /**
     * Get shops by owner
     */
    ApiResponse getShopsByOwner(Long ownerId);

    /**
     * Search shops by name
     */
    ApiResponse searchShops(String searchTerm);
}
