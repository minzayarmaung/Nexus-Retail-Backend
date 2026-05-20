package com.nexusretail.feature.user.service.impl;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.common.exception.EmailAlreadyExistsException;
import com.nexusretail.common.utils.PasswordGenerator;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.user.dto.request.UserCreateRequest;
import com.nexusretail.feature.user.dto.response.UserCreateResponse;
import com.nexusretail.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse createUser(UserCreateRequest userCreateRequest) {
        User user = userRepository.findByEmail(userCreateRequest.email())
                .orElseThrow(() -> new EmailAlreadyExistsException("Email already exists"));

        if(userCreateRequest.generatePassword()){
            String generatedPassword = PasswordGenerator.generate(userCreateRequest.username());
            user.setPassword(passwordEncoder.encode(generatedPassword));
        } else {
            user.setPassword(passwordEncoder.encode(userCreateRequest.password()));
        }

        user.setUsername(userCreateRequest.username());
        user.setEmail(userCreateRequest.email());
        user.setFirstName(userCreateRequest.firstName());
        user.setLastName(userCreateRequest.lastName());
        user.setCannotChangePassword(userCreateRequest.cannotChangePassword());

        userRepository.save(user);

        UserCreateResponse response = UserCreateResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        return ApiResponse.builder()
                .success(1)
                .data(response)
                .message("User created successfully")
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
}
