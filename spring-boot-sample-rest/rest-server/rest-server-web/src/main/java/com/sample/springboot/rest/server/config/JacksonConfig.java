package com.sample.springboot.rest.server.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;

@Configuration
public class JacksonConfig {

    /**
     * 自定义 Jackson ObjectMapper
     *
     * https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-customize-the-jackson-objectmapper
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer configureJackson2() {

        return jackson2ObjectMapperBuilder -> {
            // 属性值为null 不参与序列化
            jackson2ObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
            // 下划线转驼峰
            jackson2ObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            // 启用 pretty printer
            jackson2ObjectMapperBuilder.indentOutput(true);

            // 将标准Java类名称用作类型标识符，将类型标识作为属性值序列化。
            TypeResolverBuilder<?> typeResolver = ObjectMapper.DefaultTypeResolverBuilder.construct(NON_FINAL, LaissezFaireSubTypeValidator.instance)
                    .init(JsonTypeInfo.Id.CLASS, null)
                    .inclusion(JsonTypeInfo.As.PROPERTY);
            jackson2ObjectMapperBuilder.defaultTyping(typeResolver);
        };
    }

}
