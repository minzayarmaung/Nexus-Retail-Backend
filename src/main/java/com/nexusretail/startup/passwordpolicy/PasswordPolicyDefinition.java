package com.nexusretail.startup.passwordpolicy;

public record PasswordPolicyDefinition(
        String regex,
        String description,
        boolean active,
        String key
){}
