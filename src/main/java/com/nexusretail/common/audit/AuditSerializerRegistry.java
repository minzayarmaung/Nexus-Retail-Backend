package com.nexusretail.common.audit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditSerializerRegistry {

    private final List<AuditPayloadSerializer<?>> serializers; // Spring injects all impls
    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public <T> String serialize(T payload) {
        if (payload == null) return null;
        try {
            AuditPayloadSerializer<T> serializer = (AuditPayloadSerializer<T>) serializers.stream()
                    .filter(s -> s.supports(payload.getClass()))
                    .findFirst()
                    .orElse(null);

            JsonNode node = serializer != null
                    ? serializer.serialize(payload)
                    : objectMapper.valueToTree(payload);  // default fallback

            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            log.warn("Serialization failed for type={}", payload.getClass().getSimpleName());
            return null;
        }
    }
}