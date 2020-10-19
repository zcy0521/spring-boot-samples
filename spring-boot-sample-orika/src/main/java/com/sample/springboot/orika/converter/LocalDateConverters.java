package com.sample.springboot.orika.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConverters {

    /**
     * {@link ma.glasnost.orika.converter.builtin.DateToStringConverter}.
     */
    public static class LocalDateToStringConverter extends BidirectionalConverter<LocalDate, String> {

        private final String pattern;
        private final ThreadLocal<DateTimeFormatter> dateTimeFormats = new ThreadLocal<>();

        /* (non-Javadoc)
         * @see ma.glasnost.orika.converter.builtin.DateToStringConverter#getDateFormat()
         */
        private DateTimeFormatter getDateTimeFormat() {
            DateTimeFormatter formatter = dateTimeFormats.get();
            if (formatter == null) {
                formatter = DateTimeFormatter.ofPattern(pattern);
                dateTimeFormats.set(formatter);
            }
            return formatter;
        }

        public LocalDateToStringConverter() {
            this("yyyy-MM-dd");
        }

        public LocalDateToStringConverter(final String format) {
            this.pattern = format;
        }

        @Override
        public String convertTo(LocalDate source, Type<String> type) {
            if (null == source) {
                return null;
            }
            return getDateTimeFormat().format(source);
        }

        @Override
        public LocalDate convertFrom(String source, Type<LocalDate> type) {
            if (null == source) {
                return null;
            }

            try {
                return LocalDate.parse(source, getDateTimeFormat());
            } catch (DateTimeParseException ex) {
                return null;
            }
        }

    }

    public static class LocalDateTimeToStringConverter extends BidirectionalConverter<LocalDateTime, String> {

        private final String pattern;
        private final ThreadLocal<DateTimeFormatter> dateTimeFormats = new ThreadLocal<>();

        /* (non-Javadoc)
         * @see ma.glasnost.orika.converter.builtin.DateToStringConverter#getDateFormat()
         */
        private DateTimeFormatter getDateTimeFormat() {
            DateTimeFormatter formatter = dateTimeFormats.get();
            if (formatter == null) {
                formatter = DateTimeFormatter.ofPattern(pattern);
                dateTimeFormats.set(formatter);
            }
            return formatter;
        }

        public LocalDateTimeToStringConverter() {
            this("yyyy-MM-dd'T'HH:mm");
        }

        public LocalDateTimeToStringConverter(final String format) {
            this.pattern = format;
        }

        @Override
        public String convertTo(LocalDateTime source, Type<String> type) {
            if (null == source) {
                return null;
            }
            return getDateTimeFormat().format(source);
        }

        @Override
        public LocalDateTime convertFrom(String source, Type<LocalDateTime> type) {
            if (null == source) {
                return null;
            }

            try {
                return LocalDateTime.parse(source, getDateTimeFormat());
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

}
