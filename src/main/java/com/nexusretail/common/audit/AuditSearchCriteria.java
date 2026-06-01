package com.nexusretail.common.audit;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
public class AuditSearchCriteria {

    private String action;
    private String entityName;
    private Long   entityId;
    private String makerName;
    private String processingResult;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant to;

    private String actionMethod;
    private String browserName;
    private String deviceModel;
    private String operatingSystem;
    private String operatingSystemVersion;
}