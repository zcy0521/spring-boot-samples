package com.sample.springboot.cache.redis.mapper;

import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.config.MyBatisConfig;
import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.example.OrderExample;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@MybatisTest
@Import({MyBatisConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class OrderMapperTest {

    @Autowired
    private OrderMapper mapper;

    @Test
    public void testInsert() {
        OrderDO order = new OrderDO();
        order.setUserId(1L);
        order.setSubject("测试订单");
        order.setTotalAmount(new BigDecimal("222.33"));
        int count = mapper.insert(order);
        assertThat(count, is(1));
        assertThat(order.getId(), notNullValue());
    }

    @Test
    public void testUpdate() {
        OrderDO order = new OrderDO();
        order.setId(1L);
        order.setSubject("测试订单-改");
        order.setTotalAmount(new BigDecimal("6666.66"));
        int count = mapper.update(order);
        assertThat(count, is(1));

        order = mapper.selectById(1L);
        assertThat(order.getUserId(), notNullValue());
        assertThat(order.getSubject(), is("测试订单-改"));
        assertThat(order.getTotalAmount(), is(new BigDecimal("6666.66")));
    }

    @Test
    public void testSelectAll() {
        List<OrderDO> orders = mapper.selectAll();
        assertThat(orders, not(empty()));
    }

    @Test
    public void testSelectAllByExample() {
        OrderExample example = OrderExample.builder()
                .subject("订单")
                .build();
        List<OrderDO> orders = mapper.selectAllByExample(example);
        assertThat(orders, not(empty()));
    }

    @Test
    public void testSelectById() {
        Long id = 1L;
        OrderDO order = mapper.selectById(id);
        assertThat(order, notNullValue());

        order = mapper.selectById(null);
        assertThat(order, nullValue());
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;
        int count = mapper.deleteById(id);
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
        List<OrderDO> orders = mapper.selectAll();
        assertThat(orders, is(empty()));
    }

}
