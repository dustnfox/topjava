package ru.javawebinar.topjava.web.formatter.localdate;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LocalDataFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<LocalDateFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Collections.singleton(LocalDate.class));
    }

    @Override
    public Printer<?> getPrinter(LocalDateFormat annotation, Class<?> fieldType) {
        return new LocalDataFormatter();
    }

    @Override
    public Parser<?> getParser(LocalDateFormat annotation, Class<?> fieldType) {
        return new LocalDataFormatter();
    }
}
