package com.sample.springboot.cache.redis.mapper;

import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.config.MyBatisConfig;
import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.example.UserExample;
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
public class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    public void testInsert() {
        UserDO user = new UserDO();
        user.setUserName("测试用户");
        user.setDeptId(1L);
        int count = mapper.insert(user);
        assertThat(count, is(1));
        assertThat(user.getId(), notNullValue());
    }

    @Test
    public void testUpdate() {
        UserDO user = new UserDO();
        user.setId(1L);
        user.setUserName("测试用户-改");
        int count = mapper.update(user);
        assertThat(count, is(1));

        user = mapper.selectById(1L);
        assertThat(user.getUserName(), is("测试用户-改"));
        assertThat(user.getDeptId(), notNullValue());
    }

    @Test
    public void testSelectAll() {
        List<UserDO> users = mapper.selectAll();
        assertThat(users, not(empty()));
    }

    @Test
    public void testSelectAllByExample() {
        UserExample example = UserExample.builder()
                .userName("用户")
                .build();
        List<UserDO> users = mapper.selectAllByExample(example);
        int count = mapper.countByExample(example);
        assertThat(users, hasSize(count));
    }

    @Test
    public void testSelectAllByIds() {
        List<UserDO> users = mapper.selectAllByIds(Sets.newHashSet(1L, 2L));
        assertThat(users, hasSize(2));

        users = mapper.selectAllByIds(null);
        assertThat(users, hasSize(0));

        users = mapper.selectAllByIds(Sets.newHashSet());
        assertThat(users, hasSize(0));
    }

    @Test
    public void testSelectById() {
        UserDO user = mapper.selectById(1L);
        assertThat(user, notNullValue());

        user = mapper.selectById(null);
        assertThat(user, nullValue());
    }

    @Test
    public void testDeleteById() {
        int count = mapper.deleteById(1L);
        assertThat(count, is(1));

        count = mapper.deleteById(null);
        assertThat(count, is(0));
    }

    @Test
    public void testDeleteByIds() {
        int count = mapper.deleteByIds(Sets.newHashSet(1L, 2L));
        assertThat(count, is(2));

        count = mapper.deleteByIds(null);
        assertThat(count, is(0));

        count = mapper.deleteByIds(Sets.newHashSet());
        assertThat(count, is(0));
    }

    @Test
    public void testDeleteAll() {
        mapper.deleteAll();
        List<UserDO> users = mapper.selectAll();
        assertThat(users, is(empty()));
    }

}
