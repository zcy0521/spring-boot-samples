package com.sample.springboot.cache.redis.mapper;

import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.config.MyBatisConfig;
import com.sample.springboot.cache.redis.domain.UserRoleDO;
import com.sample.springboot.cache.redis.example.UserRoleExample;
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
public class UserRoleMapperTest {

    @Autowired
    private UserRoleMapper mapper;

    @Test
    public void testInsert() {
        UserRoleDO userRole = new UserRoleDO();
        userRole.setUserId(1L);
        userRole.setRoleId(1L);
        int count = mapper.insert(userRole);
        assertThat(count, is(1));
        assertThat(userRole.getId(), notNullValue());
    }

    @Test
    public void testSelectAll() {
        List<UserRoleDO> userRoles = mapper.selectAll();
        assertThat(userRoles, not(empty()));
    }

    @Test
    public void testSelectAllByExample() {
        UserRoleExample example = UserRoleExample.builder()
                .userId(1L)
                .build();
        List<UserRoleDO> userRoles = mapper.selectAllByExample(example);
        assertThat(userRoles, not(empty()));

        example = UserRoleExample.builder()
                .userIds(Sets.newHashSet(1L, 2L))
                .build();
        userRoles = mapper.selectAllByExample(example);
        assertThat(userRoles, not(empty()));
    }

    @Test
    public void testDeleteAll() {
        mapper.deleteAll();
        List<UserRoleDO> userRoles = mapper.selectAll();
        assertThat(userRoles, is(empty()));
    }

}
