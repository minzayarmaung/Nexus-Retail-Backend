package com.nexusretail.features.system.menu.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MenuTreeDTO(
        Long id,
        String code,
        String name,
        String route,
        String icon,
        List<MenuTreeDTO> children
) {}