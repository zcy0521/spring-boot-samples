package com.sample.springboot.alipay.controller;

import com.sample.springboot.alipay.enums.AlipayProductCode;
import com.sample.springboot.alipay.enums.OrderStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CommonController {

    /**
     * 页面添加Enums
     */
    @ModelAttribute
    public void addEnums(Model model) {
        model.addAttribute("AlipayProductCode", AlipayProductCode.class);
        model.addAttribute("AlipayProductCodeValues", AlipayProductCode.values());
        model.addAttribute("OrderStatus", OrderStatus.class);
        model.addAttribute("OrderStatusValues", OrderStatus.values());
    }

}
