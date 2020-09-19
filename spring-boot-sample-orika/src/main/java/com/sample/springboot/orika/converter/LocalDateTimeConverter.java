package com.sample.springboot.orika.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeConverter extends BidirectionalConverter<LocalDateTime, String> {

    private final String pattern;

    private final ThreadLocal<DateTimeFormatter> format = new ThreadLocal<>();

    public LocalDateTimeConverter() {
        this.pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = format.get();
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            format.set(formatter);
        }
    }

    public LocalDateTimeConverter(String pattern) {
        this.pattern = pattern;
        DateTimeFormatter formatter = format.get();
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            format.set(formatter);
        }
    }

    @Override
    public String convertTo(LocalDateTime source, Type<String> type) {
        if (null == source) {
            return null;
        }
        return format.get().format(source);
    }

    @Override
    public LocalDateTime convertFrom(String source, Type<LocalDateTime> type) {
        if (null == source) {
            return null;
        }

        try {
            return LocalDateTime.parse(source, format.get());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(String.format("Cannot convert LocalDateTime of value: %s", source));
        }
    }
}
