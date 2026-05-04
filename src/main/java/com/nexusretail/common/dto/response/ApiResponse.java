package com.nexusretail.common.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
@Schema(description = "Standard API response wrapper")
public class ApiResponse { // POJO
    @Schema(description = "Success status (1 for success, 0 for failure)", example = "1")
    private int success;

    @Schema(description = "HTTP status code", example = "200")
    private int code;

    @Schema(description = "Additional metadata", example = "{\"method\": \"POST\", \"endpoint\": \"/api/auth/login\"}")
    private Map<String, Object> meta;

    @Schema(description = "Response data payload")
    private Object data;

    @Schema(description = "Response message", example = "Login successful")
    private String message;
}