package com.sample.springboot.view.velocity;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {MybatisAutoConfiguration.class})
public class VelocityApplication {

    public static void main(String[] args) {
        SpringApplication.run(VelocityApplication.class, args);
    }

}
