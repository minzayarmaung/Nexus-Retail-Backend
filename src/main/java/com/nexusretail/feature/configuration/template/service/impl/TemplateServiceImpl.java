package com.nexusretail.feature.configuration.template.service.impl;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.configuration.template.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    @Override
    public ApiResponse getTemplateData(String name) {
        return null;
    }
}
