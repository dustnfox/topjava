package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeFilter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public DateTimeFilter(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this.startDate = startDate == null ? LocalDate.MIN : startDate;
        this.endDate = endDate == null ? LocalDate.MAX : endDate;
        this.startTime = startTime == null ? LocalTime.MIN : startTime;
        this.endTime = endTime == null ? LocalTime.MAX : endTime;
    }

    public DateTimeFilter(String startDate, String endDate, String startTime, String endTime) {
        this(
                parseLocalDate(startDate),
                parseLocalDate(endDate),
                parseLocalTime(startTime),
                parseLocalTime(endTime)
        );
    }

    private static LocalDate parseLocalDate(String date) {
        if (date != null) {
            try {
                return LocalDate.parse(date, DATE_FORMATTER);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    private static LocalTime parseLocalTime(String time) {
        if (time != null) {
            try {
                return LocalTime.parse(time, TIME_FORMATTER);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    public boolean isBetween(LocalDateTime dt) {
        return isBeetweenDate(dt.toLocalDate()) && isBeetweenTime(dt.toLocalTime());
    }

    private boolean isBeetweenTime(LocalTime lt) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    private boolean isBeetweenDate(LocalDate ld) {
        return ld.compareTo(startDate) >= 0 && ld.compareTo(endDate) <= 0;
    }

    public boolean isEmpty() {
        return startDate == LocalDate.MIN
                && endDate == LocalDate.MAX
                && startTime == LocalTime.MIN
                && endTime == LocalTime.MAX;
    }
}
