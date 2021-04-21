package com.sample.springboot.rest.server.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // CSRF
            // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#csrf
            // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-csrf
            .csrf(csrf -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringAntMatchers("/api/**")
            )
            // Response Headers
            // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#headers-cache-control
            // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-headers-cache-control
            .headers(headers -> headers
                    .cacheControl(withDefaults())
            );
    }

}
