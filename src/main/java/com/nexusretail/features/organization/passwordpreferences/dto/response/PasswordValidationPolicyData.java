package com.nexusretail.features.organization.passwordpreferences.dto.response;

import lombok.Builder;

@Builder
public record PasswordValidationPolicyData(
        long id,
        String regex,
        String description,
        String key,
        boolean active
){}

