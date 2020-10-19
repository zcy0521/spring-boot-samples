package com.sample.springboot.cache.redis.mapper;

import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.config.MyBatisConfig;
import com.sample.springboot.cache.redis.domain.RoleDO;
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
public class RoleMapperTest {

    @Autowired
    private RoleMapper mapper;

    @Test
    public void testInsert() {
        RoleDO role = new RoleDO();
        role.setRoleName("测试角色");
        int count = mapper.insert(role);
        assertThat(count, is(1));
        assertThat(role.getId(), notNullValue());
    }

    @Test
    public void testUpdate() {
        RoleDO role = new RoleDO();
        role.setId(1L);
        role.setRoleName("测试角色-改");
        int count = mapper.update(role);
        assertThat(count, is(1));

        role = mapper.selectById(1L);
        assertThat(role.getRoleName(), is("测试角色-改"));
    }

    @Test
    public void testSelectAll() {
        List<RoleDO> roles = mapper.selectAll();
        assertThat(roles, not(empty()));
    }

    @Test
    public void testSelectAllByIds() {
        List<RoleDO> roles = mapper.selectAllByIds(Sets.newHashSet(1L, 2L));
        assertThat(roles, hasSize(2));

        roles = mapper.selectAllByIds(null);
        assertThat(roles, hasSize(0));

        roles = mapper.selectAllByIds(Sets.newHashSet());
        assertThat(roles, hasSize(0));
    }

    @Test
    public void testSelectById() {
        Long id = 1L;
        RoleDO role = mapper.selectById(id);
        assertThat(role, notNullValue());

        role = mapper.selectById(null);
        assertThat(role, nullValue());
    }

    @Test
    public void testDeleteAll() {
        mapper.deleteAll();
        List<RoleDO> roles = mapper.selectAll();
        assertThat(roles, is(empty()));
    }

}
