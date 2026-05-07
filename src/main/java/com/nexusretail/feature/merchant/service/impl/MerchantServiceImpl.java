package com.nexusretail.feature.merchant.service.impl;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.Shop;
import com.nexusretail.data.models.ShopAddress;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.data.repositories.ShopAddressRepository;
import com.nexusretail.data.repositories.ShopRepository;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.merchant.dto.request.CreateMerchantRequest;
import com.nexusretail.feature.merchant.dto.request.CreateShopRequest;
import com.nexusretail.feature.merchant.dto.request.UpdateShopRequest;
import com.nexusretail.feature.merchant.dto.response.ShopAddressResponse;
import com.nexusretail.feature.merchant.dto.response.ShopResponse;
import com.nexusretail.feature.merchant.service.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShopRepository shopRepository;
    private final ShopAddressRepository shopAddressRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
    private static final String OWNER = "OWNER";

    @Override
    public ApiResponse createShopOwner(CreateMerchantRequest request) {
        try {
            // Validate that current user is SYSTEM_ADMIN
            User currentUser = getCurrentUser();
            if (!SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                return ResponseUtils.createErrorResponse("Only SYSTEM_ADMIN can create shop owners", 403);
            }

            // Validate input
            if (request.username() == null || request.username().trim().isEmpty()) {
                return ResponseUtils.createErrorResponse("Username is required", 400);
            }
            if (request.email() == null || request.email().trim().isEmpty()) {
                return ResponseUtils.createErrorResponse("Email is required", 400);
            }
            if (request.password() == null || request.password().trim().isEmpty()) {
                return ResponseUtils.createErrorResponse("Password is required", 400);
            }
            if (request.phoneNo() == null || request.phoneNo().trim().isEmpty()) {
                return ResponseUtils.createErrorResponse("Phone number is required", 400);
            }
            if (request.shopId() == null) {
                return ResponseUtils.createErrorResponse("Shop ID is required", 400);
            }

            // Check if username already exists
            if (userRepository.findByUsername(request.username().trim()).isPresent()) {
                return ResponseUtils.createErrorResponse("Username already exists: " + request.username(), 400);
            }

            // Check if email already exists
            if (userRepository.findByEmail(request.email().trim()).isPresent()) {
                return ResponseUtils.createErrorResponse("Email already exists: " + request.email(), 400);
            }

            Shop shop = shopRepository.findById(request.shopId())
                    .orElseThrow(() -> new RuntimeException("Shop not found with id: " + request.shopId()));

            // Find if Role Already Exist
            Role ownerRole = roleRepository.findByName(OWNER)
                    .orElseGet(() -> {
                        Role newRole = Role.builder()
                                .name(OWNER)
                                .build();
                        return roleRepository.save(newRole);
                    });

            // Create shop owner user
            User shopOwner = User.builder()
                    .username(request.username().trim())
                    .email(request.email().trim())
                    .password(passwordEncoder.encode(request.password()))
                    .phoneNo(request.phoneNo().trim())
                    .shopId(request.shopId())
                    .role(ownerRole)
                    .build();

            // Setting Owner
            shop.setOwnerId(shopOwner.getId());
            
            shopRepository.save(shop);
            User savedUser = userRepository.save(shopOwner);
            log.info("Shop owner created successfully: {} for shop: {}", savedUser.getUsername(), request.shopId());

            // Return user details (without password)
            var userResponse = new Object() {
                public final Long id = savedUser.getId();
                public final String username = savedUser.getUsername();
                public final String email = savedUser.getEmail();
                public final String phoneNo = savedUser.getPhoneNo();
                public final Long shopId = savedUser.getShopId();
                public final String role = savedUser.getRole().getName();
            };

            return ResponseUtils.createSuccessResponse("Shop owner created successfully", userResponse);

        } catch (Exception e) {
            log.error("Error creating shop owner: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to create shop owner: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse createShop(CreateShopRequest request) {
        try {
            // Validate that current user is SYSTEM_ADMIN
            User currentUser = getCurrentUser();
            if (!SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                return ResponseUtils.createErrorResponse("Only SYSTEM_ADMIN can create shops", 403);
            }

            // Validate input
            if (request.name() == null || request.name().trim().isEmpty()) {
                return ResponseUtils.createErrorResponse("Shop name is required", 400);
            }
            if (request.type() == null || request.type().trim().isEmpty()) {
                return ResponseUtils.createErrorResponse("Shop type is required", 400);
            }
            if (request.addresses() == null || request.addresses().isEmpty()) {
                return ResponseUtils.createErrorResponse("At least one address is required", 400);
            }

            // Check if shop name already exists
            if (shopRepository.findByName(request.name().trim()).isPresent()) {
                return ResponseUtils.createErrorResponse("Shop with this name already exists: " + request.name(), 400);
            }

            // Create shop
            Shop shop = Shop.builder()
                    .name(request.name().trim())
                    .type(request.type().trim())
                    .phoneNo(request.phoneNo())
                    .shopPhotoUrl(request.shopPhotoUrl())
                    .build();

            Shop savedShop = shopRepository.save(shop);

            // Create addresses
            List<ShopAddress> addresses = request.addresses().stream()
                    .map(addr -> ShopAddress.builder()
                            .address(addr.address())
                            .addressType(addr.addressType())
                            .city(addr.city())
                            .state(addr.state())
                            .postalCode(addr.postalCode())
                            .country(addr.country())
                            .latitude(addr.latitude())
                            .longitude(addr.longitude())
                            .shopId(savedShop.getId())
                            .createdAt(savedShop.getCreatedAt())
                            .updatedAt(savedShop.getUpdatedAt())
                            .build())
                    .collect(Collectors.toList());

            shopAddressRepository.saveAll(addresses);

            log.info("Shop created successfully: {}", savedShop.getName());

            ShopResponse response = buildShopResponse(savedShop);
            return ResponseUtils.createSuccessResponse("Shop created successfully", response);

        } catch (Exception e) {
            log.error("Error creating shop: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to create shop: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse getAllShops() {
        try {
            User currentUser = getCurrentUser();
            List<Shop> shops;

            if (SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                // SYSTEM_ADMIN can view all shops
                shops = shopRepository.findAll();
            } else if (OWNER.equals(currentUser.getRole().getName())) {
                // OWNER can view only their shop
                shops = shopRepository.findByOwnerId(currentUser.getId());
            } else {
                return ResponseUtils.createErrorResponse("Insufficient permissions to view shops", 403);
            }

            List<ShopResponse> responses = shops.stream()
                    .map(this::buildShopResponse)
                    .collect(Collectors.toList());

            return ResponseUtils.createSuccessResponse("Shops retrieved successfully", responses);

        } catch (Exception e) {
            log.error("Error retrieving shops: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve shops: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse getShopById(Long id) {
        try {
            User currentUser = getCurrentUser();

            return shopRepository.findById(id)
                    .map(shop -> {
                        // Check if user has permission to view this shop
                        if (!hasPermissionToAccessShop(currentUser, shop)) {
                            throw new SecurityException("Access denied: Cannot view shop");
                        }

                        ShopResponse response = buildShopResponse(shop);
                        return ResponseUtils.createSuccessResponse("Shop retrieved successfully", response);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Shop not found with id: " + id, 404));

        } catch (SecurityException e) {
            return ResponseUtils.createErrorResponse(e.getMessage(), 403);
        } catch (Exception e) {
            log.error("Error retrieving shop: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve shop: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse updateShop(Long id, UpdateShopRequest request) {
        try {
            User currentUser = getCurrentUser();

            // SYSTEM_ADMIN can update any shop, OWNER can only update their own shop
            if (!SYSTEM_ADMIN.equals(currentUser.getRole().getName()) &&
                !OWNER.equals(currentUser.getRole().getName())) {
                return ResponseUtils.createErrorResponse("Insufficient permissions to update shops", 403);
            }

            return shopRepository.findById(id)
                    .map(shop -> {
                        // Check if user has permission to update this shop
                        if (!hasPermissionToAccessShop(currentUser, shop)) {
                            throw new SecurityException("Access denied: Cannot update shop");
                        }

                        // Update only provided fields (PATCH behavior)
                        if (StringUtils.hasText(request.name())) {
                            shop.setName(request.name().trim());
                        }
                        if (StringUtils.hasText(request.type())) {
                            shop.setType(request.type().trim());
                        }
                        if (request.phoneNo() != null) {
                            shop.setPhoneNo(request.phoneNo());
                        }
                        if (request.shopPhotoUrl() != null) {
                            shop.setShopPhotoUrl(request.shopPhotoUrl());
                        }
                        if (request.ownerId() != null) {
                            // Verify new owner exists and has OWNER role
                            User newOwner = userRepository.findById(request.ownerId())
                                    .orElseThrow(() -> new IllegalArgumentException("New owner not found"));
                            if (!OWNER.equals(newOwner.getRole().getName())) {
                                throw new IllegalArgumentException("New owner must have OWNER role");
                            }
                            shop.setOwnerId(request.ownerId());
                        }

                        Shop updatedShop = shopRepository.save(shop);

                        // Update addresses if provided
                        if (request.addresses() != null && !request.addresses().isEmpty()) {
                            updateShopAddresses(updatedShop.getId(), request.addresses());
                        }

                        log.info("Shop updated successfully: {}", id);

                        ShopResponse response = buildShopResponse(updatedShop);
                        return ResponseUtils.createSuccessResponse("Shop updated successfully", response);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Shop not found with id: " + id, 404));

        } catch (SecurityException e) {
            return ResponseUtils.createErrorResponse(e.getMessage(), 403);
        } catch (Exception e) {
            log.error("Error updating shop: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to update shop: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse deleteShop(Long id) {
        try {
            User currentUser = getCurrentUser();

            // Only SYSTEM_ADMIN can delete shops
            if (!SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                return ResponseUtils.createErrorResponse("Only SYSTEM_ADMIN can delete shops", 403);
            }

            return shopRepository.findById(id)
                    .map(shop -> {
                        // Delete associated addresses first
                        shopAddressRepository.deleteByShopId(id);

                        // Delete the shop
                        shopRepository.delete(shop);
                        log.info("Shop deleted successfully: {}", id);
                        return ResponseUtils.createSuccessResponse("Shop deleted successfully", null);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Shop not found with id: " + id, 404));

        } catch (Exception e) {
            log.error("Error deleting shop: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to delete shop: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse getShopsByOwner(Long ownerId) {
        try {
            User currentUser = getCurrentUser();

            // SYSTEM_ADMIN can view all shops, OWNER can only view their own
            if (!SYSTEM_ADMIN.equals(currentUser.getRole().getName()) &&
                !currentUser.getId().equals(ownerId)) {
                return ResponseUtils.createErrorResponse("Access denied: Cannot view other owner's shops", 403);
            }

            List<Shop> shops = shopRepository.findByOwnerId(ownerId);
            List<ShopResponse> responses = shops.stream()
                    .map(this::buildShopResponse)
                    .collect(Collectors.toList());

            return ResponseUtils.createSuccessResponse("Shops retrieved successfully", responses);

        } catch (Exception e) {
            log.error("Error retrieving shops by owner: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve shops: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse searchShops(String searchTerm) {
        try {
            User currentUser = getCurrentUser();
            List<Shop> shops;

            if (SYSTEM_ADMIN.equals(currentUser.getRole().getName())) {
                // SYSTEM_ADMIN can search all shops
                if (StringUtils.hasText(searchTerm)) {
                    shops = shopRepository.findByNameContaining(searchTerm.trim());
                } else {
                    shops = shopRepository.findAll();
                }
            } else if (OWNER.equals(currentUser.getRole().getName())) {
                // OWNER can search only their shops
                List<Shop> ownerShops = shopRepository.findByOwnerId(currentUser.getId());
                if (StringUtils.hasText(searchTerm)) {
                    shops = ownerShops.stream()
                            .filter(shop -> shop.getName().toLowerCase().contains(searchTerm.toLowerCase().trim()))
                            .collect(Collectors.toList());
                } else {
                    shops = ownerShops;
                }
            } else {
                return ResponseUtils.createErrorResponse("Insufficient permissions to search shops", 403);
            }

            List<ShopResponse> responses = shops.stream()
                    .map(this::buildShopResponse)
                    .collect(Collectors.toList());

            return ResponseUtils.createSuccessResponse("Shops search completed successfully", responses);

        } catch (Exception e) {
            log.error("Error searching shops: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to search shops: " + e.getMessage(), 500);
        }
    }

    // Helper methods

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }

    private boolean hasPermissionToAccessShop(User currentUser, Shop shop) {
        String roleName = currentUser.getRole().getName();

        if (SYSTEM_ADMIN.equals(roleName)) {
            return true; // SYSTEM_ADMIN can access all shops
        } else if (OWNER.equals(roleName)) {
            return currentUser.getId().equals(shop.getOwnerId()); // OWNER can access only their shops
        }

        return false; // Other roles have no access
    }

    private void updateShopAddresses(Long shopId, List<com.nexusretail.feature.merchant.dto.request.UpdateShopAddressRequest> addressRequests) {
        // Delete existing addresses
        shopAddressRepository.deleteByShopId(shopId);

        // Create new addresses
        List<ShopAddress> addresses = addressRequests.stream()
                .map(addr -> ShopAddress.builder()
                        .address(addr.address())
                        .addressType(addr.addressType())
                        .city(addr.city())
                        .state(addr.state())
                        .postalCode(addr.postalCode())
                        .country(addr.country())
                        .latitude(addr.latitude())
                        .longitude(addr.longitude())
                        .shopId(shopId)
                        .build())
                .collect(Collectors.toList());

        shopAddressRepository.saveAll(addresses);
    }

    private ShopResponse buildShopResponse(Shop shop) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Get owner name
        String ownerName = "";
        try {
            User owner = userRepository.findById(shop.getOwnerId()).orElse(null);
            if (owner != null) {
                ownerName = owner.getUsername();
            }
        } catch (Exception e) {
            log.warn("Could not fetch owner name for shop: {}", shop.getId());
        }

        // Get addresses
        List<ShopAddress> addresses = shopAddressRepository.findByShopId(shop.getId());
        List<ShopAddressResponse> addressResponses = addresses.stream()
                .map(addr -> ShopAddressResponse.builder()
                        .id(addr.getId())
                        .address(addr.getAddress())
                        .addressType(addr.getAddressType())
                        .city(addr.getCity())
                        .state(addr.getState())
                        .postalCode(addr.getPostalCode())
                        .country(addr.getCountry())
                        .latitude(addr.getLatitude())
                        .longitude(addr.getLongitude())
                        .shopId(addr.getShopId())
                        .createdAt(LocalDateTime.parse(addr.getCreatedAt().format(formatter)))
                        .updatedAt(LocalDateTime.parse(addr.getUpdatedAt().format(formatter)))
                        .build())
                .collect(Collectors.toList());

        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .type(shop.getType())
                .phoneNo(shop.getPhoneNo())
                .shopPhotoUrl(shop.getShopPhotoUrl())
                .ownerId(shop.getOwnerId())
                .ownerName(ownerName)
                .addresses(addressResponses)
                .build();
    }
}
