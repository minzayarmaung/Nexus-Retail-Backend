package com.nexusretail.features.system.menu.controller;

import com.nexusretail.data.models.Role;
import com.nexusretail.data.models.User;
import com.nexusretail.features.system.menu.dto.MenuTreeDTO;
import com.nexusretail.features.system.menu.service.MenuAccessService;
import com.nexusretail.security.UserDetailServiceImpl;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("${api.base.path}/system/system-menus")
@RequiredArgsConstructor
public class MenuAccessController {

    private final MenuAccessService menuAccessService;

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<List<MenuTreeDTO>> getAllMenus() {
        return ResponseEntity.ok(menuAccessService.getAllMenusAsTree());
    }

    @GetMapping("/{roleId}/menu-access")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<List<MenuTreeDTO>> getRoleMenus(@PathVariable Long roleId) {
        return ResponseEntity.ok(menuAccessService.getMenuTreeForRole(roleId));
    }

    @PutMapping("/{roleId}/menu-access")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<Void> updateRoleMenus(
            @PathVariable Long roleId,
            @RequestBody Set<Long> menuIds) {
        menuAccessService.updateRoleMenuAccess(roleId, menuIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth/my-menus")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MenuTreeDTO>> getMyMenus(@AuthenticationPrincipal User user) {
        Set<Role> roleIds = user.getRoles();
        return ResponseEntity.ok(menuAccessService.getMenuTreeForRoles(roleIds.stream().map(Role::getId).collect(toSet())));
    }
}