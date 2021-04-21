package com.sample.springboot.rest.server.orika.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.text.DecimalFormat;
import java.text.ParseException;

public class NumericConverters {

    public static class BigDecimalToStringConverter extends BidirectionalConverter<Number, String> {

        private final String pattern;
        private final ThreadLocal<DecimalFormat> decimalFormats = new ThreadLocal<>();

        /* (non-Javadoc)
         * @see ma.glasnost.orika.converter.builtin.DateToStringConverter#getDateFormat()
         */
        private DecimalFormat getDecimalFormat() {
            DecimalFormat formatter = decimalFormats.get();
            if (formatter == null) {
                formatter = new DecimalFormat(pattern);
                decimalFormats.set(formatter);
            }
            return formatter;
        }

        public BigDecimalToStringConverter() {
            this("#.00");
        }

        public BigDecimalToStringConverter(final String format) {
            this.pattern = format;
        }

        @Override
        public String convertTo(Number source, Type<String> type) {
            if (null == source) {
                return null;
            }
            return getDecimalFormat().format(source);
        }

        @Override
        public Number convertFrom(String source, Type<Number> type) {
            if (null == source) {
                return null;
            }

            try {
                return getDecimalFormat().parse(source);
            } catch (ParseException e) {
                return null;
            }
        }

    }

}
