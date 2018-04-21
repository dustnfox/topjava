package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LocalDateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<LocalDateTimeFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(
                LocalDate.class,
                LocalTime.class,
                LocalDateTime.class
        ));
    }

    @Override
    public Printer<?> getPrinter(LocalDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(LocalDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    private Formatter<?> getFormatter(LocalDateTimeFormat annnotation, Class<?> fieldType) {
        switch (annnotation.type()) {
            case DATE:
                return new LocalDateFormatter();
            case TIME:
                return new LocalTimeFormatter();
            case DATE_TIME:
                return new LocalDateTimeFormatter();
            default:
                throw new IllegalStateException("Can't find formatter of the type.");
        }
    }
}
