package com.nexusretail.feature.shop.service;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.shop.dto.request.CreateShopOwnerRequest;
import org.springframework.stereotype.Service;

@Service
public interface ShopOwnerService {

    /**
     * Create a new shop owner (SYSTEM_ADMIN only)
     * - Creates a user with OWNER role
     * - Assigns the specified shopId
     */
    ApiResponse createShopOwner(CreateShopOwnerRequest request);
}
