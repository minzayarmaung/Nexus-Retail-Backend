package com.nexusretail.common.service.emailService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailTemplateLoader {

    private final ResourceLoader resourceLoader;

    public String load(String templatePath, Map<String, String> variables) {
        try {
            Resource resource = resourceLoader.getResource(templatePath);
            String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            for (Map.Entry<String, String> entry : variables.entrySet()) {
                content = content.replace(entry.getKey(), entry.getValue());
            }

            return content;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template: " + templatePath, e);
        }
    }
}