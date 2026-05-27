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
        @Index(name = "index_action_name", columnList = "action_name"),
        @Index(name = "index_maker_id", columnList = "maker_id"),
        @Index(name = "index_made_on_date", columnList = "made_on_date")
})
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action_name;
    private String entity_name;
    private Long entity_id;
    private Long maker_id;
    private String maker_name;

    private Timestamp made_on_date;

    private String processing_result;

    @Column(columnDefinition = "TEXT")
    private String command_as_json;

    private String api_url;
    private String ip_address;
    private String error_message;

    private String browser_name;
    private String operating_system;
    private String operating_system_version;
    private String device_model;
}
