package com.nexusretail.feature.configuration.managecode.service.impl;

import com.nexusretail.common.constant.BaseEnum;
import com.nexusretail.common.constant.Status;
import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.data.models.Code;
import com.nexusretail.data.models.CodeValue;
import com.nexusretail.data.repositories.CodeRepository;
import com.nexusretail.data.repositories.CodeValueRepository;
import com.nexusretail.feature.configuration.managecode.dto.request.CodeValueRequest;
import com.nexusretail.feature.configuration.managecode.dto.response.CodeValueResponse;
import com.nexusretail.feature.configuration.managecode.service.CodeValueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CodeValueServiceImpl implements CodeValueService {

    private final CodeValueRepository codeValueRepository;
    private final CodeRepository codeRepository;

    @Override
    public ApiResponse createCodeValue(CodeValueRequest request) {
        try {
            // Verify code exists
            Code code = codeRepository.findById(request.codeId())
                    .orElseThrow(() -> new RuntimeException("Code not found with id: " + request.codeId()));

            // Check if value is unique for this code
            if (codeValueRepository.findByCodeIdAndValue(request.codeId(), request.value()).isPresent()) {
                return ResponseUtils.createErrorResponse(
                        "Value already exists for this code type: " + request.value(), 400);
            }

            CodeValue codeValue = CodeValue.builder()
                    .code(code)
                    .value(request.value())
                    .display(request.display())
                    .description(request.description())
                    .orderPosition(request.orderPosition())
                    .status(request.status() != null ? BaseEnum.fromValue(Status.class, request.status()) : Status.ACTIVE) // Default to ACTIVE if not provided
                    .build();

            CodeValue savedCodeValue = codeValueRepository.save(codeValue);
            log.info("CodeValue created successfully for Code ID: {}", request.codeId());

            CodeValueResponse response = buildCodeValueResponse(savedCodeValue);
            return ResponseUtils.createSuccessResponse("CodeValue created successfully", response);

        } catch (Exception e) {
            log.error("Error creating code value: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to create code value: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getCodeValuesByCodeId(Long codeId) {
        try {
            // Verify code exists
            if (!codeRepository.existsById(codeId)) {
                return ResponseUtils.createErrorResponse("Code not found with id: " + codeId, 404);
            }

            List<CodeValue> codeValues = codeValueRepository.findByCodeIdOrderByOrderPositionAsc(codeId);
            List<CodeValueResponse> responses = codeValues.stream()
                    .map(this::buildCodeValueResponse)
                    .collect(Collectors.toList());

            return ResponseUtils.createSuccessResponse("CodeValues retrieved successfully", responses);

        } catch (Exception e) {
            log.error("Error retrieving code values: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve code values: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getCodeValueById(Long id) {
        try {
            return codeValueRepository.findById(id)
                    .map(codeValue -> {
                        CodeValueResponse response = buildCodeValueResponse(codeValue);
                        return ResponseUtils.createSuccessResponse("CodeValue retrieved successfully", response);
                    })
                    .orElse(ResponseUtils.createErrorResponse("CodeValue not found with id: " + id, 404));

        } catch (Exception e) {
            log.error("Error retrieving code value: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve code value: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse updateCodeValue(Long id, CodeValueRequest request) {
        try {
            return codeValueRepository.findById(id)
                    .map(codeValue -> {
                        // If changing value, check uniqueness
                        if (!codeValue.getValue().equals(request.value()) &&
                            codeValueRepository.findByCodeIdAndValue(request.codeId(), request.value()).isPresent()) {
                            throw new RuntimeException("Value already exists for this code type: " + request.value());
                        }

                        codeValue.setValue(request.value());
                        codeValue.setDisplay(request.display());
                        codeValue.setDescription(request.description());
                        codeValue.setOrderPosition(request.orderPosition());
                        codeValue.setStatus(request.status() != null ? BaseEnum.fromValue(Status.class, request.status()) : codeValue.getStatus());

                        CodeValue updatedCodeValue = codeValueRepository.save(codeValue);
                        log.info("CodeValue updated successfully: {}", id);

                        CodeValueResponse response = buildCodeValueResponse(updatedCodeValue);
                        return ResponseUtils.createSuccessResponse("CodeValue updated successfully", response);
                    })
                    .orElse(ResponseUtils.createErrorResponse("CodeValue not found with id: " + id, 404));

        } catch (Exception e) {
            log.error("Error updating code value: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to update code value: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse deleteCodeValue(Long id) {
        try {
            return codeValueRepository.findById(id)
                    .map(codeValue -> {
                        codeValueRepository.delete(codeValue);
                        log.info("CodeValue deleted successfully: {}", id);
                        return ResponseUtils.createSuccessResponse("CodeValue deleted successfully", null);
                    })
                    .orElse(ResponseUtils.createErrorResponse("CodeValue not found with id: " + id, 404));

        } catch (Exception e) {
            log.error("Error deleting code value: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to delete code value: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getDropdownOptions(String codeType) {
        try {
            // Get code by type
            Code code = codeRepository.findByCodeType(codeType)
                    .orElseThrow(() -> new RuntimeException("Code type not found: " + codeType));

            // Get all code values for this code, ordered by position
            List<CodeValue> codeValues = codeValueRepository.findByCodeIdOrderByOrderPositionAsc(code.getId());
            List<CodeValueResponse> responses = codeValues.stream()
                    .map(this::buildCodeValueResponse)
                    .collect(Collectors.toList());

            return ResponseUtils.createSuccessResponse("Dropdown options retrieved successfully", responses);

        } catch (Exception e) {
            log.error("Error retrieving dropdown options: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve dropdown options: " + e.getMessage(), 500);
        }
    }

    private CodeValueResponse buildCodeValueResponse(CodeValue codeValue) {
        return CodeValueResponse.builder()
                .id(codeValue.getId())
                .codeId(codeValue.getCode().getId())
                .value(codeValue.getValue())
                .display(codeValue.getDisplay())
                .description(codeValue.getDescription())
                .orderPosition(codeValue.getOrderPosition())
                .status(codeValue.getStatus().name())
                .build();
    }
}
