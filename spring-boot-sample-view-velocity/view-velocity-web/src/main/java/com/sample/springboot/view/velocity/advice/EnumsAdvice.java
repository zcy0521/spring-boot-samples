package com.sample.springboot.view.velocity.advice;

import com.sample.springboot.view.velocity.enums.SampleEnum;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class EnumsAdvice {

    @ModelAttribute
    public void addEnums(Model model) {
        // SampleEnum
        model.addAttribute("SampleEnum", SampleEnum.class);
        model.addAttribute("sampleEnums", SampleEnum.values());
    }

}
