package com.clinicmanager.util;

import com.clinicmanager.exception.ValidationException;

import java.util.Locale;
import java.util.regex.Pattern;

/** Small validation and cleaning rules shared by several services. */
public final class InputRules {

    public static final int MIN_PASSWORD_LENGTH = 6;
    /** BCrypt only reads the first 72 bytes; longer passwords would be silently truncated. */
    public static final int MAX_PASSWORD_LENGTH = 72;

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private InputRules() { }

    /** Trimmed text, or ValidationException when null or blank. */
    public static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required.");
        }
        return value.trim();
    }

    /** Trim and lowercase, so "Sara@X.com" and "sara@x.com" are the same account. */
    public static String normalizeEmail(String email) {
        String clean = requireText(email, "Email").toLowerCase(Locale.ROOT);
        if (!EMAIL.matcher(clean).matches()) {
            throw new ValidationException("Invalid email address.");
        }
        return clean;
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new ValidationException(
                    "Password must be at most " + MAX_PASSWORD_LENGTH + " characters.");
        }
    }

    public static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
