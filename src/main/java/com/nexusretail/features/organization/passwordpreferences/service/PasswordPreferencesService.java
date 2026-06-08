package com.nexusretail.features.organization.passwordpreferences.service;

import com.nexusretail.features.organization.passwordpreferences.dto.response.PasswordValidationPolicyData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PasswordPreferencesService {
    ResponseEntity<PasswordValidationPolicyData> getPasswordPreferences();
}
