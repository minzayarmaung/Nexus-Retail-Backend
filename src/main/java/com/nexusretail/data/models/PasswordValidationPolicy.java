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
        name = "password_validation_policy",
        indexes = {
                @Index(name = "idx_password_validation_policy_name", columnList = "name", unique = true)
        }
)
public class PasswordValidationPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regex;

    private String description;

    private boolean active;

    private String key;
}
