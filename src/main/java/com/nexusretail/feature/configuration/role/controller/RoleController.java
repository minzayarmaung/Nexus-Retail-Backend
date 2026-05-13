package com.nexusretail.feature.configuration.role.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/roles")
@Tag(name = "Roles Controller", description = "Manage Roles and Permissions")
public class RoleController {
}
