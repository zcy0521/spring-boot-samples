package com.sample.springboot.orika.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.text.DecimalFormat;
import java.text.ParseException;

public class NumberConverter extends BidirectionalConverter<Number, String> {

    private final String pattern;

    private final ThreadLocal<DecimalFormat> format = new ThreadLocal<>();

    public NumberConverter() {
        this("#.00");
    }

    public NumberConverter(String pattern) {
        this.pattern = pattern;
        DecimalFormat formatter = format.get();
        if (formatter == null) {
            formatter = new DecimalFormat(pattern);
            format.set(formatter);
        }
    }

    @Override
    public String convertTo(Number source, Type<String> type) {
        if (null == source) {
            return null;
        }
        return format.get().format(source);
    }

    @Override
    public Number convertFrom(String source, Type<Number> type) {
        if (null == source) {
            return null;
        }

        try {
            return format.get().parse(source);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Cannot convert SampleEnum of value: %s", source));
        }
    }

}
