package com.sample.springboot.view.velocity.controller.advice;

import com.sample.springboot.view.velocity.enums.SampleEnum;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class EnumsAdvice {

    @ModelAttribute
    public void addEnums(Model model) {
        // SampleEnum
        SampleEnum[] sampleEnums = SampleEnum.values();
        model.addAttribute("sampleEnums", sampleEnums);
    }

}
