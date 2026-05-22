package com.nexusretail.common.service.emailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final EmailSender emailSender;

    @Async
    @EventListener
    public void handleEmailEvent(EmailEvent event) {
        EmailRequest request = event.emailRequest();

        String label = switch (request) {
            case PasswordEmailRequest r -> "password-generate for " + r.to();
            case OtpEmailRequest r      -> "OTP for " + r.to();
            case WelcomeEmailRequest r  -> "welcome mail for " + r.to();
        };

        log.info("Dispatching email — {}", label);
        emailSender.send(request);
    }
}