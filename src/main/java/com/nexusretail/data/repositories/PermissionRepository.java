package com.nexusretail.data.repositories;

import com.nexusretail.data.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByCode(String code);

    @Query("SELECT p FROM Permission p ORDER BY p.grouping, p.entityName, p.actionName")
    List<Permission> findAllOrderedByGrouping();

    Optional<Permission> findByCode(String code);
}