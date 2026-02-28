package org.example.util;

import org.example.exeption.BadRequestException;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,20}$");

    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestException("Invalid email format");
        }
    }

    public static void validateUsername(String username) {
        if (username == null || !USERNAME_PATTERN.matcher(username).matches()) {
            throw new BadRequestException("Invalid username format");
        }
    }

    public static void validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new BadRequestException(fieldName + " cannot be null");
        }
    }

    public static void validateNotEmpty(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            throw new BadRequestException(fieldName + " cannot be empty");
        }
    }

    public static void validatePositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new BadRequestException(fieldName + " must be positive");
        }
    }
}