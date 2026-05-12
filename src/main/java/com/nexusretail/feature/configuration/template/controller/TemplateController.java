package com.nexusretail.feature.configuration.template.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.path}/template")
@RequiredArgsConstructor
@Tag(name = "Template - Name", description = "Template for Dropdowns")
public class TemplateController {
}
