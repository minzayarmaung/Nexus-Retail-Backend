package com.nexusretail.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message, Long id) {
        super(message);
    }
}
