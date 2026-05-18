package com.nexusretail.data.repositories;

import com.nexusretail.data.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByCode(String code);

    @Query("SELECT p FROM Permission p ORDER BY p.grouping, p.entityName, p.actionName")
    List<Permission> findAllOrderedByGrouping();

    @Query("SELECT p FROM Permission p WHERE p.code IN :codes")
    List<Permission> findAllByCodes(@Param("codes") Set<String> codes);

    Optional<Permission> findByCode(String code);
}