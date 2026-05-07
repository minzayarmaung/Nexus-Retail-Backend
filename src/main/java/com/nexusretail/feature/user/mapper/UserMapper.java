package com.nexusretail.feature.user.mapper;

import com.nexusretail.data.models.Shop;
import com.nexusretail.data.models.User;
import com.nexusretail.feature.merchant.dto.response.ShopResponse;
import com.nexusretail.feature.user.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNo(user.getPhoneNo())
                .role(user.getRole().getName())
                .shop(resolveOwnedShop(user))
                .build();
    }

    /**
     * Returns the shop only if this user is its owner.
     * A user owns a shop when shop.ownerId == user.id.
     * Returns null for all other cases (no shop, or user is just an employee/member).
     */
    private ShopResponse resolveOwnedShop(User user) {
        final Shop shop = user.getShop();

        if (shop == null) return null;
        if (shop.getOwnerId() == null) return null;

        if (!shop.getOwnerId().equals(user.getId())) return null;

        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .type(shop.getType())
                .phoneNo(shop.getPhoneNo())
                .shopPhotoUrl(shop.getShopPhotoUrl())
                .build();
    }
}