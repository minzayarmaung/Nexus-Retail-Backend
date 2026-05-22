package com.nexusretail.common.service.emailService;

import java.util.Map;

public record WelcomeEmailRequest(
        String to,
        String username
) implements EmailRequest {

    @Override
    public String subject() {
        return "Welcome to Nexus!";
    }

    @Override
    public String templatePath() {
        return "classpath:templates/emailTemplates/welcome-email.html";
    }

    @Override
    public Map<String, String> templateVariables() {
        return Map.of("{{username}}", username);
    }
}