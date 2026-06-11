package com.nexusretail.data.models;

import com.nexusretail.features.system.menu.dto.RoleMenuAccessId;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Entity
@Table(name = "role_menu_access")
@IdClass(RoleMenuAccessId.class)
@Getter
public class RoleMenuAccess {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private SystemMenu menu;

    public static RoleMenuAccess of(Role role, SystemMenu menu) {
        var access = new RoleMenuAccess();
        access.role = role;
        access.menu = menu;
        return access;
    }
}