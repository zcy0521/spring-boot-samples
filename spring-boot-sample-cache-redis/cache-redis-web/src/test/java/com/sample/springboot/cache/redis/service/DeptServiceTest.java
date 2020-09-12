package com.sample.springboot.cache.redis.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeptServiceTest {

    @Autowired
    private DeptService deptService;

    @Test
    public void testFindAll() {
        deptService.findAll();
    }

    @Test
    public void testFindById() {
        deptService.findById(119L);
    }

}
