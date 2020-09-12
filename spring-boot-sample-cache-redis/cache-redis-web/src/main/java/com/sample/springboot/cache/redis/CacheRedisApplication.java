package com.sample.springboot.cache.redis;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication(exclude = {CacheAutoConfiguration.class, RedisAutoConfiguration.class, MybatisAutoConfiguration.class})
public class CacheRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheRedisApplication.class, args);
    }

}
