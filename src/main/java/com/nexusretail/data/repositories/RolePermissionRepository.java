package com.nexusretail.data.repositories;


import com.nexusretail.data.models.Permission;
import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRole(Role role);

    boolean existsByRoleAndPermission(Role adminRole, Permission permission);
}
