package com.sample.springboot.data.mybatis.controller;

import com.sample.springboot.data.mybatis.service.FirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/first")
public class FirstController {

    @Autowired
    private FirstService firstService;

}
