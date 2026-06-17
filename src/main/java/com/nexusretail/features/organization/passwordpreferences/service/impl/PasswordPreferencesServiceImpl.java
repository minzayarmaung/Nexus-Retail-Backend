package com.nexusretail.features.organization.passwordpreferences.service.impl;

import com.nexusretail.data.models.PasswordValidationPolicy;
import com.nexusretail.data.repositories.PasswordPreferencesRepository;
import com.nexusretail.features.organization.passwordpreferences.dto.response.PasswordValidationPolicyData;
import com.nexusretail.features.organization.passwordpreferences.service.PasswordPreferencesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordPreferencesServiceImpl implements PasswordPreferencesService {

    private final PasswordPreferencesRepository passwordPreferencesRepository;

    @Override
    public Collection<PasswordValidationPolicyData> getPasswordPreferences() {
        List<PasswordValidationPolicy> data = passwordPreferencesRepository.findAll();
        if(data.isEmpty()){
            return java.util.Collections.emptyList();
        }
        return data.stream().map(policy -> PasswordValidationPolicyData.builder()
                .id(policy.getId())
                .regex(policy.getRegex())
                .description(policy.getDescription())
                .key(policy.getKey())
                .active(policy.isActive())
                .build())
                .toList();
    }

    @Transactional
    @Override
    public ResponseEntity<PasswordValidationPolicyData> updatePasswordPreferences(Long id) {
        PasswordValidationPolicy policy = passwordPreferencesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password validation policy not found"));

        passwordPreferencesRepository.deactivateOtherPolicies(id);

        policy.setActive(true);
        passwordPreferencesRepository.save(policy);

        PasswordValidationPolicyData response = PasswordValidationPolicyData.builder()
                .id(policy.getId())
                .regex(policy.getRegex())
                .description(policy.getDescription())
                .key(policy.getKey())
                .active(policy.isActive())
                .build();

        return ResponseEntity.ok(response);
    }
}
