package com.sample.springboot.view.velocity.config;

import com.alibaba.boot.velocity.VelocityLayoutProperties;
import com.alibaba.boot.velocity.web.servlet.view.EmbeddedVelocityLayoutViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Velocity 配置
 */
@Configuration
public class VelocityConfig {

    @Bean
    public EmbeddedVelocityLayoutViewResolver velocityViewResolver(VelocityLayoutProperties properties) {
        EmbeddedVelocityLayoutViewResolver resolver = new EmbeddedVelocityLayoutViewResolver();
        properties.applyToViewResolver(resolver);
        resolver.setRedirectHttp10Compatible(false);
        return resolver;
    }

}
