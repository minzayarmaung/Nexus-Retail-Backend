package com.nexusretail.common.audit;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AuditEvent(
        String action,
        String entityName,
        Long entityId,
        Object[] commandArgs,
        Object result,
        String processingResult,
        String errorMessage,
        String makerUsername,
        Long makerId,
        String apiUrl,
        String ipAddress,
        Instant madeOnDate,
        String browserName,
        String operationSystem,
        String operationSystemVersion,
        String device_model
) {}