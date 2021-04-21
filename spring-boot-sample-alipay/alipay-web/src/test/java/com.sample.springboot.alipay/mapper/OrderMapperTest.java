package com.sample.springboot.alipay.mapper;

import com.sample.springboot.alipay.config.MyBatisConfig;
import com.sample.springboot.alipay.domain.OrderDO;
import com.sample.springboot.alipay.enums.AlipayProductCode;
import com.sample.springboot.alipay.enums.OrderStatus;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    private OrderMapper orderMapper;

    @Test
    public void testInsert() {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        OrderDO order = new OrderDO();
        order.setOutTradeNo("6823789339978248");
        order.setTradeNo("2013112011001004330000121536");
        order.setTotalAmount(BigDecimal.valueOf(20.00d));
        order.setAppId("2016091700534734");
        order.setSellerId("2088101106499364");
        order.setOrderStatus(OrderStatus.ORDER_CREATE);
        order.setSubject("当面付交易");
        order.setBody("当面付交易内容");
        order.setGmtPayment(Date.from(LocalDateTime.parse("2015-04-27 15:45:57", formatter1).atZone(ZoneId.systemDefault()).toInstant()));
        order.setGmtRefund(Date.from(LocalDateTime.parse("2015-04-28 15:45:57.320", formatter2).atZone(ZoneId.systemDefault()).toInstant()));
        order.setGmtClose(Date.from(LocalDateTime.parse("2015-04-29 15:45:57", formatter1).atZone(ZoneId.systemDefault()).toInstant()));
        order.setProductCode(AlipayProductCode.FAST_INSTANT_TRADE_PAY);

        int count = orderMapper.insert(order);
        assertThat(count, is(1));
    }

}
