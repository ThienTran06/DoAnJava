package com.library.librarymanager.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Year;
import java.util.regex.Pattern;

public final class ValidationUtils {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private ValidationUtils() {
    }

    public static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw badRequest(fieldName + " khong duoc de trong");
        }
        return value.trim();
    }

    public static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static void requirePhone(String value, String fieldName) {
        String trimmed = requireText(value, fieldName);
        if (!PHONE_PATTERN.matcher(trimmed).matches()) {
            throw badRequest(fieldName + " phai gom 10 chu so va bat dau bang 0");
        }
    }

    public static void validateOptionalEmail(String value) {
        String trimmed = trimToNull(value);
        if (trimmed != null && !EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw badRequest("Email khong dung dinh dang");
        }
    }

    public static void requirePositive(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw badRequest(fieldName + " phai la so duong");
        }
    }

    public static void requirePositiveOrZero(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw badRequest(fieldName + " khong duoc am");
        }
    }

    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw badRequest(fieldName + " phai la so duong");
        }
    }

    public static void requirePositiveOrZero(int value, String fieldName) {
        if (value < 0) {
            throw badRequest(fieldName + " khong duoc am");
        }
    }

    public static void requirePublicationYear(int year) {
        int currentYear = Year.now().getValue();
        if (year < 1000 || year > currentYear) {
            throw badRequest("Nam xuat ban phai nam trong khoang 1000 den " + currentYear);
        }
    }

    public static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
