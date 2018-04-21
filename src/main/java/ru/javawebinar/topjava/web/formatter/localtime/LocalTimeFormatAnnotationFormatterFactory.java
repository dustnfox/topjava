package ru.javawebinar.topjava.web.formatter.localtime;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LocalTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<LocalTimeFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Collections.singleton(LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(LocalTimeFormat annotation, Class<?> fieldType) {
        return new LocalTimeFormatter();
    }

    @Override
    public Parser<?> getParser(LocalTimeFormat annotation, Class<?> fieldType) {
        return new LocalTimeFormatter();
    }
}
