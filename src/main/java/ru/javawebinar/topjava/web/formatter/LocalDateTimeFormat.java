package ru.javawebinar.topjava.web.formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateTimeFormat {
    FormatType type() default FormatType.DATE_TIME;

    enum FormatType {DATE, TIME, DATE_TIME}
}
