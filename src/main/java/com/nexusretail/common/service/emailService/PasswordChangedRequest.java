package com.nexusretail.common.service.emailService;

import lombok.Builder;

import java.util.Map;

@Builder
public record PasswordChangedRequest (
        String to,
        String username
) implements EmailRequest{

    @Override
    public String subject() {
        return "You have Changed the Password";
    }

    @Override
    public String templatePath() {
        return "classpath:templates/emailTemplates/password-changed-email.html";
    }

    @Override
    public Map<String, String> templateVariables() {
        return Map.of(
                "{{username}}", username
                );
    }
}
