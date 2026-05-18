package com.nexusretail.data.repositories;


import com.nexusretail.data.models.Permission;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRole(Role role);

    @Query("SELECT rp.permission.id FROM RolePermission rp WHERE rp.role.id = :roleId")
    Set<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);

    boolean existsByRoleAndPermission(Role adminRole, Permission permission);
}
