package com.nexusretail.features.system.menu.service.impl;

import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.RoleMenuAccess;
import com.nexusretail.data.models.SystemMenu;
import com.nexusretail.data.repositories.RoleMenuAccessRepository;
import com.nexusretail.data.repositories.RoleRepository;
import com.nexusretail.data.repositories.SystemMenuRepository;
import com.nexusretail.features.system.menu.dto.MenuTreeDTO;
import com.nexusretail.features.system.menu.service.MenuAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class MenuAccessServiceImpl implements MenuAccessService {

    private final SystemMenuRepository menuRepo;
    private final RoleMenuAccessRepository accessRepo;
    private final RoleRepository roleRepo;

    // ── Admin config page: single role view (unchanged contract) ──────
    @Override
    public List<MenuTreeDTO> getMenuTreeForRole(Long roleId) {
        return getMenuTreeForRoles(Set.of(roleId));
    }

    // ── User-facing: union across all assigned roles ──────────────────
    @Override
    public List<MenuTreeDTO> getMenuTreeForRoles(Set<Long> roleIds) {
        if (roleIds.isEmpty()) return List.of();

        Set<Long> grantedIds = accessRepo.findMenuIdsByRoleIds(roleIds);
        List<SystemMenu> allMenus = menuRepo.findAllActiveOrderByDisplayOrder();
        Set<Long> effectiveIds = resolveWithAncestors(grantedIds, allMenus);

        return allMenus.stream()
                .filter(m -> m.getParent() == null)
                .filter(m -> effectiveIds.contains(m.getId()))
                .map(m -> toDto(m, effectiveIds))
                .toList();
    }

    // ── getAllMenusAsTree() unchanged ─────────────────────────────────
    @Override
    public List<MenuTreeDTO> getAllMenusAsTree() {
        List<SystemMenu> allMenus = menuRepo.findAllActiveOrderByDisplayOrder();
        Set<Long> allIds = allMenus.stream().map(SystemMenu::getId).collect(toSet());
        return allMenus.stream()
                .filter(m -> m.getParent() == null)
                .map(m -> toDto(m, allIds))
                .toList();
    }

    // ── updateRoleMenuAccess() unchanged ─────────────────────────────
    @Override
    @Transactional
    public void updateRoleMenuAccess(Long roleId, Set<Long> menuIds) {
        accessRepo.deleteByRoleId(roleId);
        Role role = roleRepo.findById(roleId).orElseThrow();
        List<SystemMenu> menus = menuRepo.findAllById(menuIds);
        accessRepo.saveAll(menus.stream().map(m -> RoleMenuAccess.of(role, m)).toList());
    }

    // ── private helpers (unchanged) ───────────────────────────────────
    private Set<Long> resolveWithAncestors(Set<Long> grantedIds, List<SystemMenu> allMenus) {
        Map<Long, SystemMenu> menuMap = allMenus.stream()
                .collect(Collectors.toMap(SystemMenu::getId, m -> m));
        Set<Long> effective = new HashSet<>(grantedIds);
        grantedIds.forEach(id -> {
            SystemMenu current = menuMap.get(id);
            while (current != null && current.getParent() != null) {
                effective.add(current.getParent().getId());
                current = current.getParent();
            }
        });
        return effective;
    }

    private MenuTreeDTO toDto(SystemMenu menu, Set<Long> effectiveIds) {
        List<MenuTreeDTO> children = menu.getChildren().stream()
                .filter(c -> effectiveIds.contains(c.getId()))
                .map(c -> toDto(c, effectiveIds))
                .toList();
        return new MenuTreeDTO(menu.getId(), menu.getCode(), menu.getName(),
                menu.getRoute(), menu.getIcon(), children);
    }
}
