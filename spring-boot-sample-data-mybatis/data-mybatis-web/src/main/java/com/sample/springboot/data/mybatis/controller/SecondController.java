package com.sample.springboot.data.mybatis.controller;

import com.sample.springboot.data.mybatis.service.SecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/second")
public class SecondController {

    @Autowired
    private SecondService secondService;

}
