package com.nexusretail.common.audit;

import com.fasterxml.jackson.databind.JsonNode;

public interface AuditPayloadSerializer<T> {
    boolean supports(Class<?> type);
    JsonNode serialize(T payload);
}
