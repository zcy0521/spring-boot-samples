package com.sample.springboot.cache.redis.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;

@Configuration
public class JacksonConfig {

    /**
     * 自定义 Jackson ObjectMapper
     *
     * https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-customize-the-jackson-objectmapper
     */
    @Bean
    public Jackson2ObjectMapperBuilder configureJackson() {
        return Jackson2ObjectMapperBuilder
                .json()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .defaultTyping(typeResolver());
    }

    /**
     * 安全类型转换
     */
    @Bean
    public TypeResolverBuilder<?> typeResolver() {
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(List.class)
                .allowIfSubType(Set.class)
                .allowIfSubType(Map.class)
                .allowIfSubType("com.sample.springboot.cache.redis.model.")
                .allowIfSubTypeIsArray()
                .build();

        return ObjectMapper.DefaultTypeResolverBuilder
                .construct(NON_FINAL, ptv)
                .init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY);
    }

}
