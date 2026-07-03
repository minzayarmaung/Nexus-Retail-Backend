package com.nexusretail.common.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final String username) {
        super("User '" + username + "' does not exist");
    }
}