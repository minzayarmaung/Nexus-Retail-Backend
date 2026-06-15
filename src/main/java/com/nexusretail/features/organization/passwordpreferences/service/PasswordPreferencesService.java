package com.nexusretail.features.organization.passwordpreferences.service;

import com.nexusretail.features.organization.passwordpreferences.dto.response.PasswordValidationPolicyData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface PasswordPreferencesService {

    ResponseEntity<PasswordValidationPolicyData> updatePasswordPreferences(Long id);

    Collection<PasswordValidationPolicyData> getPasswordPreferences();
}
