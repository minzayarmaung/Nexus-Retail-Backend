package com.nexusretail.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailEventListener {

    private final EmailService emailService;

    @Autowired
    public EmailEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleEmailEvent(EmailEvent event) {
        emailService.sendEmail(
                event.getToEmail(),
                event.getSubject(),
                event.getBody()
        );
        System.out.println("Processing email event for: " + event.getToEmail());
    }
}