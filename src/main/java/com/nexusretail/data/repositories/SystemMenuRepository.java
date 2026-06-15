package com.nexusretail.data.repositories;

import com.nexusretail.data.models.SystemMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemMenuRepository extends JpaRepository<SystemMenu, Long> {
    List<SystemMenu> findAllActiveOrderByDisplayOrder(int displayOrder);
}
