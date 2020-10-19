package com.sample.springboot.cache.redis.mapper;

import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.config.MyBatisConfig;
import com.sample.springboot.cache.redis.domain.DeptDO;
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
public class DeptMapperTest {

    @Autowired
    private DeptMapper mapper;

    @Test
    public void testInsert() {
        DeptDO dept = new DeptDO();
        dept.setParentId(0L);
        dept.setDeptName("测试部门");
        int count = mapper.insert(dept);
        assertThat(count, is(1));
        assertThat(dept.getId(), notNullValue());
    }

    @Test
    public void testUpdate() {
        DeptDO dept = new DeptDO();
        dept.setId(1L);
        dept.setDeptName("测试部门-改");
        int count = mapper.update(dept);
        assertThat(count, is(1));

        dept = mapper.selectById(1L);
        assertThat(dept.getParentId(), notNullValue());
        assertThat(dept.getDeptName(), is("测试部门-改"));
    }

    @Test
    public void testSelectAll() {
        List<DeptDO> depts = mapper.selectAll();
        assertThat(depts, not(empty()));
    }

    @Test
    public void testSelectByIds() {
        List<DeptDO> depts = mapper.selectAllByIds(Sets.newHashSet(1L, 2L));
        assertThat(depts, hasSize(2));

        depts = mapper.selectAllByIds(null);
        assertThat(depts, hasSize(0));

        depts = mapper.selectAllByIds(Sets.newHashSet());
        assertThat(depts, hasSize(0));
    }

    @Test
    public void testSelectById() {
        Long id = 1L;
        DeptDO dept = mapper.selectById(id);
        assertThat(dept, notNullValue());

        dept = mapper.selectById(null);
        assertThat(dept, nullValue());
    }

    @Test
    public void testDeleteAll() {
        mapper.deleteAll();
        List<DeptDO> departments = mapper.selectAll();
        assertThat(departments, is(empty()));
    }

}
