package com.nexusretail.common.service.emailService;

import java.util.Map;

public sealed interface EmailRequest
        permits OtpEmailRequest, PasswordChangedRequest, PasswordEmailRequest, WelcomeEmailRequest {

    String to();
    String subject();
    String templatePath();
    Map<String, String> templateVariables();
}