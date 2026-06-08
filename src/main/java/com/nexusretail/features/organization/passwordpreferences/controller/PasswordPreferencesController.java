package com.nexusretail.features.organization.passwordpreferences.controller;

import com.nexusretail.features.organization.passwordpreferences.dto.response.PasswordValidationPolicyData;
import com.nexusretail.features.organization.passwordpreferences.service.PasswordPreferencesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/organization/password-preferences")
@Tag(name = "Password Preferences", description = "Manage password preferences for an organization")
public class PasswordPreferencesController {

    private final PasswordPreferencesService passwordPreferencesService;

    @GetMapping
    public ResponseEntity<PasswordValidationPolicyData> getPasswordPreferences(){
        return this.passwordPreferencesService.getPasswordPreferences();
    }

}
