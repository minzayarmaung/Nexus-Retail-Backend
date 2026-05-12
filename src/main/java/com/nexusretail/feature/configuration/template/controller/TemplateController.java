package com.nexusretail.feature.configuration.template.controller;

import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.configuration.template.dto.response.TemplateDataResponse;
import com.nexusretail.feature.configuration.template.service.TemplateService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.path}/template")
@RequiredArgsConstructor
@Tag(name = "Template Resolver", description = "Template for Dropdowns")
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getTemplate(@Parameter(description = "screen name") @RequestParam("name") final String name , HttpServletRequest request){
        final ApiResponse response = this.templateService.getTemplateData(name);
        return ResponseUtils.buildResponse(request , response);
    }
}
