package com.nexusretail.features.organization.passwordpreferences.controller;

import com.nexusretail.features.organization.passwordpreferences.dto.response.PasswordValidationPolicyData;
import com.nexusretail.features.organization.passwordpreferences.service.PasswordPreferencesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/organization/password-preferences")
@Tag(name = "Password Preferences", description = "Manage password preferences for an organization")
public class PasswordPreferencesController {

    private final PasswordPreferencesService passwordPreferencesService;

    @PreAuthorize("hasPermission(null, 'READ_PASSWORD_PREFERENCES')")
    @GetMapping
    public ResponseEntity<Collection<PasswordValidationPolicyData>> getPasswordPreferences(){
        Collection<PasswordValidationPolicyData> response = this.passwordPreferencesService.getPasswordPreferences();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission(null, 'UPDATE_PASSWORD_PREFERENCES')")
    @PatchMapping("/{id}")
    public ResponseEntity<PasswordValidationPolicyData> updatePasswordPreference(@PathVariable final Long id){
        return this.passwordPreferencesService.updatePasswordPreferences(id);
    }

}
