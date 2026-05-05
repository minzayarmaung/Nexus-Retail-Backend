package com.nexusretail.feature.configuration.managecode.service;

import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.feature.configuration.managecode.dto.request.CodeValueRequest;
import org.springframework.stereotype.Service;

@Service
public interface CodeValueService {
    ApiResponse createCodeValue(CodeValueRequest request);
    ApiResponse getCodeValuesByCodeId(Long codeId);
    ApiResponse getCodeValueById(Long id);
    ApiResponse updateCodeValue(Long id, CodeValueRequest request);
    ApiResponse deleteCodeValue(Long id);
    ApiResponse getDropdownOptions(String codeType);
}
