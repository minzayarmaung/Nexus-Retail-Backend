package com.nexusretail.common.service.emailService.impl;

import com.nexusretail.common.service.emailService.EmailRequest;
import com.nexusretail.common.service.emailService.EmailSender;
import com.nexusretail.common.service.emailService.EmailTemplateLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BrevoEmailSender implements EmailSender {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.api.url}")
    private String apiUrl;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private final RestTemplate restTemplate = new RestTemplate();
    private final EmailTemplateLoader templateLoader;

    @Override
    public void send(EmailRequest request) {
        String htmlContent = templateLoader.load(
                request.templatePath(),
                request.templateVariables()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = Map.of(
                "sender",      Map.of("email", senderEmail, "name", senderName),
                "to",          List.of(Map.of("email", request.to())),
                "subject",     request.subject(),
                "htmlContent", htmlContent
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        restTemplate.postForEntity(apiUrl, entity, String.class);
    }
}
