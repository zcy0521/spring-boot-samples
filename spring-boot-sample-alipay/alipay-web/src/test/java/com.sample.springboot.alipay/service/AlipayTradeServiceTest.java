package com.sample.springboot.alipay.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlipayTradeServiceTest {

    @Resource(name = "alipayTradeService")
    private AlipayTradeService tradeService;

    @Resource(name = "alipayTradeService2")
    private AlipayTradeService tradeService2;

    @Test
    public void testWapPay() {
        String tradeNo = tradeService.appPay("aaaaaaaaaaa");
        log.info(tradeNo);
    }

    @Test
    public void testWapPay2() {
        String tradeNo = tradeService2.appPay("aaaaaaaaaaa");
        log.info(tradeNo);
    }

}
