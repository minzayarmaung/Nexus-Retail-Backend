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
@Table(name = "codes", indexes = {
        @Index(name = "index_code_type", columnList = "code_type")
})
public class Code extends Auditable {

    @Column(nullable = false, unique = true)
    private String codeType;

    @Column(length = 500)
    private String description;
}
