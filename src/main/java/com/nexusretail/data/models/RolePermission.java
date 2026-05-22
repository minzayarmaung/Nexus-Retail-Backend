package com.nexusretail.data.models;

import com.nexusretail.data.common.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RolePermissionId.class)
@Table(name = "role_permission", indexes = {
        @Index(name = "index_role_permission_role_id", columnList = "role_id"),
        @Index(name = "index_role_permission_permission_id", columnList = "permission_id")
})
public class RolePermission {

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permission;

    public static RolePermission of(Role role, Permission permission) {
        RolePermission rp = new RolePermission();
        rp.setRoleId(role.getId());
        rp.setPermissionId(permission.getId());
        rp.setRole(role);
        rp.setPermission(permission);
        return rp;
    }
}
