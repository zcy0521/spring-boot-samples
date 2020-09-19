package com.sample.springboot.orika.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalTimeConverter extends BidirectionalConverter<LocalTime, String> {

    private final String pattern;

    private final ThreadLocal<DateTimeFormatter> format = new ThreadLocal<>();

    public LocalTimeConverter() {
        this.pattern = "HH:mm";
        DateTimeFormatter formatter = format.get();
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            format.set(formatter);
        }
    }

    public LocalTimeConverter(String pattern) {
        this.pattern = pattern;
        DateTimeFormatter formatter = format.get();
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            format.set(formatter);
        }
    }

    @Override
    public String convertTo(LocalTime source, Type<String> type) {
        if (null == source) {
            return null;
        }
        return format.get().format(source);
    }

    @Override
    public LocalTime convertFrom(String source, Type<LocalTime> type) {
        if (null == source) {
            return null;
        }

        try {
            return LocalTime.parse(source, format.get());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(String.format("Cannot convert LocalTime of value: %s", source));
        }
    }
}
