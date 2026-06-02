package com.nexusretail.common.service.emailService;

import java.util.Map;

public record OtpEmailRequest(
        String to,
        String otpCode
) implements EmailRequest {

    @Override
    public String subject() {
        return "Your OTP Code";
    }

    @Override
    public String templatePath() {
        return "classpath:templates/emailTemplates/otp-email.html";
    }

    @Override
    public Map<String, String> templateVariables() {
        return Map.of("{{otpCode}}", otpCode);
    }
}