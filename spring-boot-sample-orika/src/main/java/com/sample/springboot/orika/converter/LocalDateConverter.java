package com.sample.springboot.orika.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConverter extends BidirectionalConverter<LocalDate, String> {

    private final String pattern;

    private final ThreadLocal<DateTimeFormatter> format = new ThreadLocal<>();

    public LocalDateConverter() {
        this.pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = format.get();
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            format.set(formatter);
        }
    }

    public LocalDateConverter(String pattern) {
        this.pattern = pattern;
        DateTimeFormatter formatter = format.get();
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            format.set(formatter);
        }
    }

    @Override
    public String convertTo(LocalDate source, Type<String> type) {
        if (null == source) {
            return null;
        }
        return format.get().format(source);
    }

    @Override
    public LocalDate convertFrom(String source, Type<LocalDate> type) {
        if (null == source) {
            return null;
        }

        try {
            return LocalDate.parse(source, format.get());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(String.format("Cannot convert LocalDate of value: %s", source));
        }
    }

}
