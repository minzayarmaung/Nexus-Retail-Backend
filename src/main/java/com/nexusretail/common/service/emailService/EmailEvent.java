package com.nexusretail.common.service.emailService;
import org.springframework.context.ApplicationEvent;

public final class EmailEvent extends ApplicationEvent {

    private final EmailRequest emailRequest;

    public EmailEvent(Object source, EmailRequest emailRequest) {
        super(source);
        this.emailRequest = emailRequest;
    }

    public EmailRequest emailRequest() {
        return emailRequest;
    }
}