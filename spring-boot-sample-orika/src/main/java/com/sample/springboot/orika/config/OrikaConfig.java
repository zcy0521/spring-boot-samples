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

        // 注册全局 converter
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        converterFactory.registerConverter(new LocalDateConverter());
        converterFactory.registerConverter(new LocalDateTimeConverter());
        converterFactory.registerConverter(new NumberConverter());
        converterFactory.registerConverter(new SampleEnumConverter());

        return mapperFactory;
    }

}
