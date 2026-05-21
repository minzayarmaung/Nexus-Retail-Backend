package com.nexusretail.feature.user.service.impl;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.common.exception.EmailAlreadyExistsException;
import com.nexusretail.common.exception.UserNotFoundException;
import com.nexusretail.common.service.impl.BrevoEmailService;
import com.nexusretail.common.utils.PasswordGenerator;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.feature.user.dto.request.UserCreateRequest;
import com.nexusretail.feature.user.dto.response.UserCreateResponse;
import com.nexusretail.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BrevoEmailService mailSender;

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
        if(userCreateRequest.generatePassword()){
            generatedPassword = PasswordGenerator.generate(userCreateRequest.username());
            user.setPassword(passwordEncoder.encode(generatedPassword));
            mailSender.sendEmail(userCreateRequest.username() , userCreateRequest.email() , generatedPassword);
            message = "User Created Successfully with Generated Password. Please check your email for the password.";
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
        if(user.isExpired()) {
            return "User with id " + id + " is already suspended.";
        }
        user.setExpired(true);
        userRepository.save(user);
        return "User with id " + id + " has been suspended successfully.";
    }
}
