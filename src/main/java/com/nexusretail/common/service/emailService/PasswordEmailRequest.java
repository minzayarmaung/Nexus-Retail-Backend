package com.nexusretail.common.service.emailService;
import java.util.Map;

public record PasswordEmailRequest(
        String to,
        String username,
        String password
) implements EmailRequest {

    @Override
    public String subject() {
        return "Your New Account Password";
    }

    @Override
    public String templatePath() {
        return "classpath:templates/emailTemplates/password-email.html";
    }

    @Override
    public Map<String, String> templateVariables() {
        return Map.of(
                "{{username}}", username,
                "{{password}}", password
        );
    }
}

