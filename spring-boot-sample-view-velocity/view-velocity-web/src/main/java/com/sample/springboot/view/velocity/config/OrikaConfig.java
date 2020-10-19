package com.sample.springboot.view.velocity.config;

import com.sample.springboot.view.velocity.orika.converter.LocalDateConverters;
import com.sample.springboot.view.velocity.orika.converter.EnumsConverters;
import com.sample.springboot.view.velocity.orika.converter.NumericConverters;
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
        converterFactory.registerConverter(new LocalDateConverters.LocalDateToStringConverter());
        converterFactory.registerConverter(new LocalDateConverters.LocalDateTimeToStringConverter());
        converterFactory.registerConverter(new NumericConverters.BigDecimalToStringConverter());
        converterFactory.registerConverter(new EnumsConverters.SampleEnumToIntegerConverter());
        return mapperFactory;
    }

}
