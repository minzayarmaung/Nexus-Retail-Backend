package com.nexusretail.features.configuration.managecode.service;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.features.configuration.managecode.dto.request.CodeRequest;
import org.springframework.stereotype.Service;

@Service
public interface CodeService {
    ApiResponse createCode(CodeRequest request);
    ApiResponse getAllCodes();
    ApiResponse getCodeById(Long id);
    ApiResponse getCodeByType(String codeType);
    ApiResponse updateCode(Long id, CodeRequest request);
    ApiResponse deleteCode(Long id);
}
