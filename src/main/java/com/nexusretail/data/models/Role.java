package com.nexusretail.data.models;

import com.nexusretail.data.common.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "roles", indexes = {
    @Index(name = "index_role_name", columnList = "name")
})
public class Role extends Auditable {

    @Column(nullable = false, unique = true)
    private String name; // SYSTEM_ADMIN, OWNER, HR, SALESPERSON
}
