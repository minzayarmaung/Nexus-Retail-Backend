package com.nexusretail.feature.user.service.impl;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.common.exception.EmailAlreadyExistsException;
import com.nexusretail.common.exception.UserNotFoundException;
import com.nexusretail.common.service.emailService.EmailEvent;
import com.nexusretail.common.service.emailService.PasswordEmailRequest;
import com.nexusretail.common.utils.PasswordGenerator;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.user.dto.request.UserCreateRequest;
import com.nexusretail.feature.user.dto.request.UserUpdateRequest;
import com.nexusretail.feature.user.dto.response.UserCreateResponse;
import com.nexusretail.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ApiResponse createUser(UserCreateRequest userCreateRequest) {
        userRepository.findByEmail(userCreateRequest.email())
                .ifPresent(existingUser -> {
                    throw new EmailAlreadyExistsException("Email already exists: " + userCreateRequest.email());
                });

        String message = "User Created Successfully";
        User user = new User();
        String generatedPassword = "";
        if (userCreateRequest.generatePassword()) {
            generatedPassword = PasswordGenerator.generate(userCreateRequest.username());
            user.setGeneratedPassword(true);
            user.setPassword(passwordEncoder.encode(generatedPassword));
            eventPublisher.publishEvent(
                    new EmailEvent(this, new PasswordEmailRequest(
                            userCreateRequest.email(),
                            userCreateRequest.username(),
                            generatedPassword
                    ))
            );
            message = "User Created Successfully with Generated Password. Please check your email for the password.";
        } else {
            user.setPassword(passwordEncoder.encode(userCreateRequest.password()));
        }

        user.setUsername(userCreateRequest.username());
        user.setEmail(userCreateRequest.email());
        user.setFirstName(userCreateRequest.firstName());
        user.setLastName(userCreateRequest.lastName());
        user.setFirstTimeLogin(true);
        if(userCreateRequest.cannotChangePassword()){
            user.setFirstTimeLogin(false);
        }
        user.setCannotChangePassword(userCreateRequest.cannotChangePassword());

        Set<Role> roles = resolveRoles(userCreateRequest.roles());
        user.setRoles(roles);

        userRepository.save(user);

        UserCreateResponse response = UserCreateResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .build();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(response)
                .message(message)
                .build();
    }

    @Override
    public Boolean checkUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        return userRepository.existsByUsername(username);
    }

    @Override
    public String generatePassword(String username) {
        if (username == null || username.isBlank()) {
            return PasswordGenerator.generate();
        }
        return PasswordGenerator.generate(username);
    }

    @Override
    public String suspendUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        if (user.isExpired()) {
            return "User with id " + id + " is already suspended.";
        }
        user.setExpired(true);
        userRepository.save(user);
        return "User with id " + id + " has been suspended successfully.";
    }

    @Override
    public ApiResponse updateUser(UserUpdateRequest userUpdateRequest, Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id));

        if(userUpdateRequest.email() != null && !userUpdateRequest.email().equals(user.getEmail())) {
            Optional<User> existingUserWithEmail = userRepository.findByEmail(userUpdateRequest.email());
            if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(id)) {
                throw new EmailAlreadyExistsException("Email already exists: " + userUpdateRequest.email());
            }
            user.setEmail(userUpdateRequest.email());
        }

        if(userUpdateRequest.username() != null && !userUpdateRequest.username().equals(user.getUsername())) {
            Optional<User> existingUserWithUsername = userRepository.findByUsername(userUpdateRequest.username());
            if (existingUserWithUsername.isPresent() && !existingUserWithUsername.get().getId().equals(id)) {
                throw new IllegalArgumentException("Username already exists: " + userUpdateRequest.username());
            }
            user.setUsername(userUpdateRequest.username());
        }

        if(userUpdateRequest.firstName() != null && !userUpdateRequest.firstName().equals(user.getFirstName())) {
            user.setFirstName(userUpdateRequest.firstName());
        }

        if(userUpdateRequest.lastName() != null && !userUpdateRequest.lastName().equals(user.getLastName())) {
            user.setLastName(userUpdateRequest.lastName());
        }

        if(userUpdateRequest.cannotChangePassword()) {
            user.setCannotChangePassword(false);
        } else {
            user.setCannotChangePassword(true);
        }

        if(userUpdateRequest.roles() != null && !userUpdateRequest.roles().isEmpty()) {
            Set<Role> roles = resolveRoles(userUpdateRequest.roles());
            user.setRoles(roles);
        }

        userRepository.save(user);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(user.getUsername())
                .message("User updated successfully")
                .build();
    }

    private Set<Role> resolveRoles(List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Default role USER not found"));
            return Set.of(defaultRole);
        }

        return roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + name)))
                .collect(Collectors.toSet());
    }
}
