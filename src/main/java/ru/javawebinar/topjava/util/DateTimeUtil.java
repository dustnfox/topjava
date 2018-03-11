package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static <T extends Comparable<T>> boolean isBetween(T value, T startValue, T endValue) {
        return value.compareTo(startValue) >= 0 && value.compareTo(endValue) <= 0;
    }

    public static LocalDate dateOf(String date, LocalDate defaultValue) {
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException | NullPointerException ignored) {
        }
        return defaultValue;
    }

    public static LocalTime timeOf(String time, LocalTime defaultValue) {
        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException | NullPointerException ignored) {
        }
        return defaultValue;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
