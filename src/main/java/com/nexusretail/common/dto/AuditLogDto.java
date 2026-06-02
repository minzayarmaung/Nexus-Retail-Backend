package com.nexusretail.common.dto;

import com.nexusretail.data.models.AuditLog;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class AuditLogDto {

    private Long      id;
    private String    actionMethod;
    private String    actionName;
    private String    entityName;
    private Long      entityId;
    private Long      makerId;
    private String    makerName;
    private Timestamp madeOnDate;
    private String    processingResult;
    private String    commandAsJson;
    private String    apiUrl;
    private String    ipAddress;
    private String    errorMessage;
    private String    browserName;
    private String    operatingSystem;
    private String    operatingSystemVersion;
    private String    deviceModel;

    public static AuditLogDto from(AuditLog log) {
        return AuditLogDto.builder()
                .id(log.getId())
                .actionMethod(log.getActionMethod())
                .actionName(log.getActionName())
                .entityName(log.getEntityName())
                .entityId(log.getEntityId())
                .makerId(log.getMakerId())
                .makerName(log.getMakerName())
                .madeOnDate(log.getMadeOnDate())
                .processingResult(log.getProcessingResult())
                .commandAsJson(log.getCommandAsJson())
                .apiUrl(log.getApiUrl())
                .ipAddress(log.getIpAddress())
                .errorMessage(log.getErrorMessage())
                .browserName(log.getBrowserName())
                .operatingSystem(log.getOperatingSystem())
                .operatingSystemVersion(log.getOperatingSystemVersion())
                .deviceModel(log.getDeviceModel())
                .build();
    }
}