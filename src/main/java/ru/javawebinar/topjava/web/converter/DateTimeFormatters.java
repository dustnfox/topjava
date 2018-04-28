package ru.javawebinar.topjava.web.converter;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

public class DateTimeFormatters {
    public static class LocalDateFormatter implements Formatter<LocalDate> {

        @Override
        public LocalDate parse(String text, Locale locale) {
            return parseLocalDate(text);
        }

        @Override
        public String print(LocalDate lt, Locale locale) {
            return lt.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public static class LocalTimeFormatter implements Formatter<LocalTime> {

        @Override
        public LocalTime parse(String text, Locale locale) {
            return parseLocalTime(text);
        }

        @Override
        public String print(LocalTime lt, Locale locale) {
            return lt.format(DateTimeFormatter.ISO_LOCAL_TIME);
        }
    }

    public static class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
        private final static DateTimeFormatter DATETIMEPICKER_FMT = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm");

        @Override
        public LocalDateTime parse(String text, Locale locale) {
            return parseLocalDateTime(text, DATETIMEPICKER_FMT);
        }

        @Override
        public String print(LocalDateTime ldt, Locale locale) {
            return ldt.format(DATETIMEPICKER_FMT);
        }
    }
}
