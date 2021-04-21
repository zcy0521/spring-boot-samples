package com.sample.springboot.view.velocity.advice;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * 全局数据绑定 (跨Controller)
 * https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web.html#mvc-ann-initbinder
 * https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web.html#mvc-ann-controller-advice
 */
@ControllerAdvice
public class DataBinderAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // "" to null
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
