package com.nexusretail.data.repositories;

import com.nexusretail.data.models.RoleMenuAccess;
import com.nexusretail.features.system.menu.dto.RoleMenuAccessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

public interface RoleMenuAccessRepository extends JpaRepository<RoleMenuAccess, RoleMenuAccessId> {

    @Query("SELECT r.menu.id FROM RoleMenuAccess r WHERE r.role.id = :roleId")
    Set<Long> findMenuIdsByRoleId(@Param("roleId") Long roleId);

    // NEW: for multi-role users
    @Query("SELECT r.menu.id FROM RoleMenuAccess r WHERE r.role.id IN :roleIds")
    Set<Long> findMenuIdsByRoleIds(@Param("roleIds") Set<Long> roleIds);

    @Modifying
    @Query("DELETE FROM RoleMenuAccess r WHERE r.role.id = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);
}