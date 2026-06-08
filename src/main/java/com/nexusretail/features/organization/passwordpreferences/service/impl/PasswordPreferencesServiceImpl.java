package com.nexusretail.features.organization.passwordpreferences.service.impl;

import com.nexusretail.data.models.PasswordValidationPolicy;
import com.nexusretail.data.repositories.PasswordPreferencesRepository;
import com.nexusretail.features.organization.passwordpreferences.dto.response.PasswordValidationPolicyData;
import com.nexusretail.features.organization.passwordpreferences.service.PasswordPreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordPreferencesServiceImpl implements PasswordPreferencesService {

    private final PasswordPreferencesRepository passwordPreferencesRepository;

    @Override
    public ResponseEntity<PasswordValidationPolicyData> getPasswordPreferences() {
        List<PasswordValidationPolicy> data = passwordPreferencesRepository.findAll();
        if(data.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        PasswordValidationPolicy policy = data.get(0);
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
