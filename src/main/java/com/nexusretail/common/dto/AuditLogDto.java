package com.nexusretail.common.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.nexusretail.data.models.AuditLog;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Builder
public class AuditLogDto {

    private Long     id;
    private String   actionName;
    private String   entityName;
    private Long     entityId;
    private Long     makerId;
    private String   makerUsername;
    private Timestamp madeOnDate;
    private String   processingResult;
    private String commandAsJson;
    private String   apiUrl;
    private String   ipAddress;
    private String   errorMessage;

    public static AuditLogDto from(AuditLog log) {
        return AuditLogDto.builder()
                .id(log.getId())
                .actionName(log.getAction_name())
                .entityName(log.getEntity_name())
                .entityId(log.getEntity_id())
                .makerId(log.getMaker_id())
                .makerUsername(log.getMaker_name())
                .madeOnDate(log.getMade_on_date())
                .processingResult(log.getProcessing_result() != null
                        ? log.getProcessing_result()
                        : null)
                .commandAsJson(log.getCommand_as_json())
                .apiUrl(log.getApi_url())
                .ipAddress(log.getIp_address())
                .errorMessage(log.getError_message())
                .build();
    }
}
