package com.nexusretail.features.system.managereports.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.path}/system/reports")
@RequiredArgsConstructor
@Tag(name = "Audit Management", description = "Audit management APIs")
public class ManageReportController {
}
