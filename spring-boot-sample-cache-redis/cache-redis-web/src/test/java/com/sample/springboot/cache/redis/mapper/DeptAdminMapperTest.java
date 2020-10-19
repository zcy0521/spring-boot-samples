package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.config.MyBatisConfig;
import com.sample.springboot.cache.redis.domain.DeptAdminDO;
import com.sample.springboot.cache.redis.enums.Position;
import com.sample.springboot.cache.redis.example.DeptAdminExample;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@MybatisTest
@Import({MyBatisConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class DeptAdminMapperTest {

    @Autowired
    private DeptAdminMapper mapper;

    @Test
    public void testEnum() {
        DeptAdminDO deptAdmin = new DeptAdminDO();
        deptAdmin.setDeptId(1L);
        deptAdmin.setUserId(1L);
        deptAdmin.setPosition(Position.POSITION_A);
        int count = mapper.insert(deptAdmin);
        assertThat(count, is(1));
        assertThat(deptAdmin.getId(), notNullValue());

        DeptAdminExample example = DeptAdminExample.builder()
                .position(Position.POSITION_A)
                .build();
        List<DeptAdminDO> deptAdmins = mapper.selectAllByExample(example);
        assertThat(deptAdmins, not(empty()));

        example = DeptAdminExample.builder()
                .positions(new Position[]{Position.POSITION_A, Position.POSITION_B})
                .build();
        deptAdmins = mapper.selectAllByExample(example);
        assertThat(deptAdmins, not(empty()));
    }

    @Test
    public void testDeleteAll() {
        mapper.deleteAll();
        List<DeptAdminDO> deptAdmins = mapper.selectAll();
        assertThat(deptAdmins, is(empty()));
    }

}
