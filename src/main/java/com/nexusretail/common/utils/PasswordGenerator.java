package com.nexusretail.common.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    private static final String UPPERCASE   = "ABCDEFGHJKLMNPQRSTUVWXYZ";      // removed I, O (confusing)
    private static final String LOWERCASE   = "abcdefghjkmnpqrstuvwxyz";        // removed i, l, o (confusing)
    private static final String DIGITS      = "23456789";                        // removed 0, 1 (confusing)
    private static final String SPECIAL     = "@#$%&*!?";
    private static final String ALL_CHARS   = UPPERCASE + LOWERCASE + DIGITS + SPECIAL;

    private static final int PASSWORD_LENGTH = 12;
    private static final int MAX_ATTEMPTS    = 100;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a secure password that does not contain any part of the username.
     *
     * @param username the username to exclude from the password
     * @return a generated password string
     */
    public static String generate(String username) {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String password = buildPassword();
            if (!containsUsername(password, username)) {
                return password;
            }
        }
        return forceExcludeUsername(username);
    }

    /**
     * Generates a secure password without username filtering.
     */
    public static String generate() {
        return buildPassword();
    }

    // -------------------------------------------------------------------------
    // Core builder
    // -------------------------------------------------------------------------

    private static String buildPassword() {
        List<Character> chars = new ArrayList<>();

        chars.add(randomChar(UPPERCASE));
        chars.add(randomChar(UPPERCASE));
        chars.add(randomChar(LOWERCASE));
        chars.add(randomChar(LOWERCASE));
        chars.add(randomChar(DIGITS));
        chars.add(randomChar(DIGITS));
        chars.add(randomChar(SPECIAL));

        int remaining = PASSWORD_LENGTH - chars.size();
        for (int i = 0; i < remaining; i++) {
            chars.add(randomChar(ALL_CHARS));
        }

        Collections.shuffle(chars, RANDOM);

        return toGoogleFormat(chars);
    }

    // -------------------------------------------------------------------------
    // Google-style format  →  xxxx-xxxx-xxxx  (groups of 4, separated by "-")
    // -------------------------------------------------------------------------

    private static String toGoogleFormat(List<Character> chars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.size(); i++) {
            if (i > 0 && i % 4 == 0) {
                sb.append('-');
            }
            sb.append(chars.get(i));
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Username exclusion helpers
    // -------------------------------------------------------------------------

    private static boolean containsUsername(String password, String username) {
        if (username == null || username.isBlank()) return false;

        String cleanPassword = password.replace("-", "").toLowerCase();
        String cleanUsername = username.toLowerCase();

        for (int len = 3; len <= cleanUsername.length(); len++) {
            for (int start = 0; start <= cleanUsername.length() - len; start++) {
                if (cleanPassword.contains(cleanUsername.substring(start, start + len))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String forceExcludeUsername(String username) {
        String safeChars = buildSafeCharset(username);
        List<Character> chars = new ArrayList<>();

        chars.add(randomChar(UPPERCASE));
        chars.add(randomChar(LOWERCASE));
        chars.add(randomChar(DIGITS));
        chars.add(randomChar(SPECIAL));

        String fallbackPool = safeChars.isEmpty() ? ALL_CHARS : safeChars;
        while (chars.size() < PASSWORD_LENGTH) {
            chars.add(randomChar(fallbackPool));
        }

        Collections.shuffle(chars, RANDOM);
        return toGoogleFormat(chars);
    }

    private static String buildSafeCharset(String username) {
        if (username == null) return ALL_CHARS;
        StringBuilder safe = new StringBuilder();
        for (char c : ALL_CHARS.toCharArray()) {
            if (username.toLowerCase().indexOf(Character.toLowerCase(c)) < 0) {
                safe.append(c);
            }
        }
        return safe.toString();
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    private static char randomChar(String source) {
        return source.charAt(RANDOM.nextInt(source.length()));
    }
}