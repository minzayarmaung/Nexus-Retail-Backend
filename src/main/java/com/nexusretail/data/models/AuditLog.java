package com.nexusretail.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "audit", indexes = {
        @Index(name = "index_action_method",  columnList = "action_method"),
        @Index(name = "index_action_name",    columnList = "action_name"),
        @Index(name = "index_maker_id",       columnList = "maker_id"),
        @Index(name = "index_made_on_date",   columnList = "made_on_date")
})
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String    actionMethod;           // DB: action_method
    private String    actionName;             // DB: action_name
    private String    entityName;             // DB: entity_name
    private Long      entityId;               // DB: entity_id
    private Long      makerId;                // DB: maker_id
    private String    makerName;              // DB: maker_name
    private Timestamp madeOnDate;             // DB: made_on_date
    private String    processingResult;       // DB: processing_result
    private String    apiUrl;                 // DB: api_url
    private String    ipAddress;              // DB: ip_address
    private String    errorMessage;           // DB: error_message
    private String    browserName;            // DB: browser_name
    private String    operatingSystem;        // DB: operating_system
    private String    operatingSystemVersion; // DB: operating_system_version
    private String    deviceModel;            // DB: device_model

    @Column(columnDefinition = "TEXT")
    private String commandAsJson;             // DB: command_as_json
}