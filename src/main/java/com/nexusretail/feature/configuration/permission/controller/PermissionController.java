package com.nexusretail.feature.configuration.permission.controller;

import com.nexusretail.feature.configuration.permission.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
}
