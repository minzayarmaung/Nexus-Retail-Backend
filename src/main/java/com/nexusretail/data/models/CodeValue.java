package com.nexusretail.data.models;

import com.nexusretail.data.common.Auditable;
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
@Table(name = "code_values", indexes = {
        @Index(name = "index_code_id", columnList = "code_id"),
        @Index(name = "index_code_value", columnList = "value")
})
public class CodeValue extends Auditable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code_id", nullable = false)
    private Code code;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private String display;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer orderPosition;
}
