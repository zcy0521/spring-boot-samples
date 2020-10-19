package com.sample.springboot.orika.config;

import com.sample.springboot.orika.converter.*;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaConfig {

    @Bean
    public MapperFactory getFactory() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();

        // DateConverter
        converterFactory.registerConverter(new LocalDateConverters.LocalDateToStringConverter());
        converterFactory.registerConverter(new LocalDateConverters.LocalDateTimeToStringConverter());

        // NumericConverter
        converterFactory.registerConverter(new NumericConverters.BigDecimalToStringConverter());

        // EnumsConverter
        converterFactory.registerConverter(new EnumsConverters.SampleEnumToIntegerConverter());

        return mapperFactory;
    }

}
