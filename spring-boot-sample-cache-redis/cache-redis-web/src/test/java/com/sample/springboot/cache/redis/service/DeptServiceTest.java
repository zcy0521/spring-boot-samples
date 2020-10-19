package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.DeptDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeptServiceTest {

    @Autowired
    private DeptService deptService;

    @Test
    public void testFindById() {
        DeptDO dept = deptService.findById(24675L);
        assertThat(dept, notNullValue());
        log.debug("部门路径: {}", dept.getPath());

        dept = deptService.findById(24678L);
        assertThat(dept, notNullValue());
        log.debug("部门路径: {}", dept.getPath());

        dept = deptService.findById(37109L);
        assertThat(dept, notNullValue());
        log.debug("部门路径: {}", dept.getPath());
    }

}
