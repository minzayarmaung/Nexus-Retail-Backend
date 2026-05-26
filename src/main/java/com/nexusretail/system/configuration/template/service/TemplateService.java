package com.nexusretail.system.configuration.template.service;

import com.nexusretail.common.dto.response.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface TemplateService {
    ApiResponse getTemplateData(String name);
}
