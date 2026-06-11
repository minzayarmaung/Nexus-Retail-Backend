package com.nexusretail.features.system.menu.service;

import com.nexusretail.features.system.menu.dto.MenuTreeDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface MenuAccessService {
    List<MenuTreeDTO> getMenuTreeForRoles(Set<Long> roleIds);

    List<MenuTreeDTO> getAllMenusAsTree();

    List<MenuTreeDTO> getMenuTreeForRole(Long userId);

    void updateRoleMenuAccess(Long roleId, Set<Long> menuIds);
}
