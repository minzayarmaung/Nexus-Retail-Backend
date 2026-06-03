package com.nexusretail.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(
        name = "custom_report",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_custom_report_name", columnNames = { "report_name" })
        },
        indexes = {
                @Index(name = "idx_custom_report_name", columnList = "report_name")
        }
)
public class CustomReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_name", nullable = false, length = 255)
    private String  report_name;

    @Column(name = "report_type")
    private String  report_type;

    @Column(name = "report_subtype")
    private String  report_subtype;

    @Column(name = "report_category")
    private String  report_category;

    @Column(name = "description")
    private String  description;

    @Column(name = "report_sql")
    private String  report_sql;

    @Column(name = "core_report")
    private boolean core_report;

    @Column(name = "use_report")
    private boolean use_report;

    @Column(name = "is_active")
    private boolean is_active;
}
