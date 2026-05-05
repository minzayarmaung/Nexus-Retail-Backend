package com.nexusretail.feature.shop.service.impl;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.shop.dto.request.CreateShopOwnerRequest;
import com.nexusretail.feature.shop.service.ShopOwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShopOwnerServiceImpl implements ShopOwnerService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
    private static final String OWNER = "OWNER";

    @Override
    public ApiResponse createShopOwner(CreateShopOwnerRequest request) {
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

            // Find or create OWNER role for this shop
            Role ownerRole = roleRepository.findByNameAndShopId(OWNER, request.shopId())
                    .orElseGet(() -> {
                        Role newRole = Role.builder()
                                .name(OWNER)
                                .shopId(request.shopId())
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
                public final String firstName = request.firstName();
                public final String lastName = request.lastName();
            };

            return ResponseUtils.createSuccessResponse("Shop owner created successfully", userResponse);

        } catch (Exception e) {
            log.error("Error creating shop owner: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to create shop owner: " + e.getMessage(), 500);
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
