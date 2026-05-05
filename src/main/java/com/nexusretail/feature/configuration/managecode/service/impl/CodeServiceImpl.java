package com.nexusretail.feature.configuration.managecode.service.impl;

import com.nexusretail.common.constant.Status;
import com.nexusretail.common.dto.ResponseUtils;
import com.nexusretail.common.dto.response.ApiResponse;
import com.nexusretail.data.models.Code;
import com.nexusretail.data.repositories.CodeRepository;
import com.nexusretail.feature.configuration.managecode.dto.request.CodeRequest;
import com.nexusretail.feature.configuration.managecode.dto.response.CodeResponse;
import com.nexusretail.feature.configuration.managecode.service.CodeService;
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
public class CodeServiceImpl implements CodeService {

    private final CodeRepository codeRepository;

    @Override
    public ApiResponse createCode(CodeRequest request) {
        try {
            // Check if code type already exists
            if (codeRepository.findByCodeType(request.codeType()).isPresent()) {
                return ResponseUtils.createErrorResponse("Code type already exists: " + request.codeType(), 400);
            }

            Code code = Code.builder()
                    .codeType(request.codeType())
                    .description(request.description())
                    .status(Status.ACTIVE)
                    .build();

            Code savedCode = codeRepository.save(code);
            log.info("Code created successfully: {}", request.codeType());

            CodeResponse response = CodeResponse.builder()
                    .id(savedCode.getId())
                    .codeType(savedCode.getCodeType())
                    .description(savedCode.getDescription())
                    .status(savedCode.getStatus().name())
                    .build();

            return ResponseUtils.createSuccessResponse("Code created successfully", response);

        } catch (Exception e) {
            log.error("Error creating code: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to create code: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getAllCodes() {
        try {
            List<Code> codes = codeRepository.findAll();
            List<CodeResponse> responses = codes.stream()
                    .map(code -> CodeResponse.builder()
                            .id(code.getId())
                            .codeType(code.getCodeType())
                            .description(code.getDescription())
                            .status(code.getStatus() != null ? code.getStatus().name() : Status.ACTIVE.name())
                            .build())
                    .collect(Collectors.toList());

            return ResponseUtils.createSuccessResponse("Codes retrieved successfully", responses);

        } catch (Exception e) {
            log.error("Error retrieving codes: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve codes: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getCodeById(Long id) {
        try {
            return codeRepository.findById(id)
                    .map(code -> {
                        CodeResponse response = CodeResponse.builder()
                                .id(code.getId())
                                .codeType(code.getCodeType())
                                .description(code.getDescription())
                                .status(code.getStatus() != null ? code.getStatus().name() : Status.ACTIVE.name())
                                .build();
                        return ResponseUtils.createSuccessResponse("Code retrieved successfully", response);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Code not found with id: " + id, 404));

        } catch (Exception e) {
            log.error("Error retrieving code by id: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve code: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getCodeByType(String codeType) {
        try {
            return codeRepository.findByCodeType(codeType)
                    .map(code -> {
                        CodeResponse response = CodeResponse.builder()
                                .id(code.getId())
                                .codeType(code.getCodeType())
                                .description(code.getDescription())
                                .status(code.getStatus() != null ? code.getStatus().name() : Status.ACTIVE.name())
                                .build();
                        return ResponseUtils.createSuccessResponse("Code retrieved successfully", response);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Code not found with type: " + codeType, 404));

        } catch (Exception e) {
            log.error("Error retrieving code by type: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to retrieve code: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse updateCode(Long id, CodeRequest request) {
        try {
            return codeRepository.findById(id)
                    .map(code -> {
                        // Check if new code type is unique (if changing it)
                        if (!code.getCodeType().equals(request.codeType()) &&
                            codeRepository.findByCodeType(request.codeType()).isPresent()) {
                            throw new RuntimeException("Code type already exists: " + request.codeType());
                        }

                        code.setCodeType(request.codeType());
                        code.setDescription(request.description());
                        Code updatedCode = codeRepository.save(code);

                        log.info("Code updated successfully: {}", id);

                        CodeResponse response = CodeResponse.builder()
                                .id(updatedCode.getId())
                                .codeType(updatedCode.getCodeType())
                                .description(updatedCode.getDescription())
                                .status(updatedCode.getStatus() != null ? updatedCode.getStatus().name() : Status.ACTIVE.name())
                                .build();

                        return ResponseUtils.createSuccessResponse("Code updated successfully", response);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Code not found with id: " + id, 404));

        } catch (Exception e) {
            log.error("Error updating code: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to update code: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResponse deleteCode(Long id) {
        try {
            return codeRepository.findById(id)
                    .map(code -> {
                        codeRepository.delete(code);
                        log.info("Code deleted successfully: {}", id);
                        return ResponseUtils.createSuccessResponse("Code deleted successfully", null);
                    })
                    .orElse(ResponseUtils.createErrorResponse("Code not found with id: " + id, 404));

        } catch (Exception e) {
            log.error("Error deleting code: {}", e.getMessage(), e);
            return ResponseUtils.createErrorResponse("Failed to delete code: " + e.getMessage(), 500);
        }
    }
}
