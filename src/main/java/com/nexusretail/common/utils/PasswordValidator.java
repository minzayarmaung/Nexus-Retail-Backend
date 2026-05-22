package com.nexusretail.common.utils;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {

    private static final int MIN_LENGTH = 12;

    public record ValidationResult(boolean valid, List<String> errors) {}

    public static ValidationResult validate(String password, String username) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.isBlank()) {
            errors.add("Password is required.");
            return new ValidationResult(false, errors);
        }

        if (password.length() < MIN_LENGTH) {
            errors.add("Password must be at least " + MIN_LENGTH + " characters.");
        }

        if (!password.chars().anyMatch(Character::isUpperCase)) {
            errors.add("Password must contain at least one uppercase letter.");
        }

        if (!password.chars().anyMatch(Character::isLowerCase)) {
            errors.add("Password must contain at least one lowercase letter.");
        }

        if (!password.chars().anyMatch(Character::isDigit)) {
            errors.add("Password must contain at least one number.");
        }

        if (!password.chars().anyMatch(c -> !Character.isLetterOrDigit(c))) {
            errors.add("Password must contain at least one special character (e.g. @, #, !).");
        }

        String lowerPassword = password.toLowerCase();
        long uniqueCount = lowerPassword.chars().distinct().count();
        if (uniqueCount < lowerPassword.length()) {
            errors.add("Password must not contain duplicate characters.");
        }

        if (username != null && !username.isBlank()) {
            if (lowerPassword.contains(username.trim().toLowerCase())) {
                errors.add("Password must not contain your username.");
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }
}
