package com.nexusretail.system.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nexusretail.common.audit.AuditPayloadSerializer;
import com.nexusretail.system.auth.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginRequestSerializer implements AuditPayloadSerializer<LoginRequest> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(Class<?> type) {
        return LoginRequest.class.isAssignableFrom(type);
    }

    @Override
    public JsonNode serialize(LoginRequest payload) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("username", payload.username());
        return node;
    }
}
