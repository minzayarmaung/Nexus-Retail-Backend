package com.nexusretail.common.service.impl;

import com.nexusretail.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BrevoEmailService implements EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ResourceLoader resourceLoader;

    @Override
    public void sendEmail(String username, String email, String password) {
        String htmlContent = loadTemplate(username, password);

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> emailRequest = Map.of(
                "sender", Map.of("email", "admin@nexus.com", "name", "Nexus Admin"),
                "to", List.of(Map.of("email", email)),
                "subject", "Your New Account Password",
                "htmlContent", htmlContent
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(emailRequest, headers);
        restTemplate.postForEntity(apiUrl, entity, String.class);
    }

    private String loadTemplate(String username, String password) {
        try {
            Resource resource = resourceLoader.getResource("classpath:templates/emailTemplates/password-email.html");
            String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            return content
                    .replace("{{username}}", username)
                    .replace("{{password}}", password);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }
}