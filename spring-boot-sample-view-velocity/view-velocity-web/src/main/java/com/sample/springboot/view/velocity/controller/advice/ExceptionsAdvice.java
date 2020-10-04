package com.sample.springboot.view.velocity.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

/**
 * 全局异常处理 (跨Controller)
 * https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web.html#mvc-ann-exceptionhandler
 * https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web.html#mvc-ann-controller-advice
 */
@ControllerAdvice
public class ExceptionsAdvice {

    @ExceptionHandler
    public ResponseEntity<String> handle(IOException ex) {
        // ...
        return null;
    }

}
