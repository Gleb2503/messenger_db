package org.example.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime date) {
        return date != null ? date.format(FORMATTER) : null;
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDateTime daysAgo(int days) {
        return LocalDateTime.now().minusDays(days);
    }
}