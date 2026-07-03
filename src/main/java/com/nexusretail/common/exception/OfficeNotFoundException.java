package com.nexusretail.common.exception;

public class OfficeNotFoundException extends RuntimeException {
    public OfficeNotFoundException(Long officeId) {
        super("Office with ID " + officeId + " not found.");
    }
}
