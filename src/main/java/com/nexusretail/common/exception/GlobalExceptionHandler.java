package com.nexusretail.common.exception;

import com.nexusretail.common.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request
    ) {

        ApiResponse response = ApiResponse.builder()
                .success(0)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .meta(Map.of(
                        "endpoint", request.getRequestURI(),
                        "method", request.getMethod()
                ))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
