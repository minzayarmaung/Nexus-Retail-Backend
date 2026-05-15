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
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String grouping;

    private String permission;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "action_name")
    private String actionName;

    @Column(name = "can_maker_checker")
    private boolean canMakerChecker;
}
