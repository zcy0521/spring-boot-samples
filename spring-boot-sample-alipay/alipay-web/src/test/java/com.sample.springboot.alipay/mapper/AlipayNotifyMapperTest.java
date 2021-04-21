package com.sample.springboot.alipay.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.sample.springboot.alipay.config.MyBatisConfig;
import com.sample.springboot.alipay.domain.AlipayNotifyDO;
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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@MybatisTest
@Import({MyBatisConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AlipayNotifyMapperTest {

    @Autowired
    private AlipayNotifyMapper notifyMapper;

    @Test
    public void testInsert() {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        AlipayNotifyDO notify = new AlipayNotifyDO();

        // 公共参数
        notify.setNotifyId("ac05099524730693a8b330c5ecf72da9786");
        notify.setNotifyTime(Date.from(LocalDateTime.parse("2016-07-19 14:10:49", formatter1).atZone(ZoneId.systemDefault()).toInstant()));
        notify.setNotifyType("trade_status_sync");
        notify.setCharset("utf-8");
        notify.setVersion("1.0");
        notify.setSignType("RSA2");
        notify.setSign("601510b7970e52cc63db0f44997cf70e");
        notify.setAuthAppId("2016091700534734");

        // 业务参数
        notify.setTradeNo("2013112011001004330000121536");
        notify.setAppId("2016091700534734");
        notify.setOutTradeNo("6823789339978248");
        notify.setOutBizNo("HZRF001");
        notify.setBuyerId("2088102122524333");
        notify.setBuyerLogonId("159﹡﹡﹡﹡﹡﹡20");
        notify.setSellerId("2088101106499364");
        notify.setSellerEmail("zhu﹡﹡﹡@alitest.com");
        notify.setTradeStatus("TRADE_CLOSED");
        notify.setTotalAmount(BigDecimal.valueOf(20.00d));
        notify.setReceiptAmount(BigDecimal.valueOf(15.00d));
        notify.setInvoiceAmount(BigDecimal.valueOf(10.00d));
        notify.setBuyerPayAmount(BigDecimal.valueOf(13.88d));
        notify.setPointAmount(BigDecimal.valueOf(12.00d));
        notify.setRefundFee(BigDecimal.valueOf(2.58d));
        notify.setSubject("当面付交易");
        notify.setBody("当面付交易内容");
        notify.setGmtCreate(Date.from(LocalDateTime.parse("2015-04-27 15:45:57", formatter1).atZone(ZoneId.systemDefault()).toInstant()));
        notify.setGmtPayment(Date.from(LocalDateTime.parse("2015-04-27 15:45:57", formatter1).atZone(ZoneId.systemDefault()).toInstant()));
        notify.setGmtRefund(Date.from(LocalDateTime.parse("2015-04-28 15:45:57.320", formatter2).atZone(ZoneId.systemDefault()).toInstant()));
        notify.setGmtClose(Date.from(LocalDateTime.parse("2015-04-29 15:45:57", formatter1).atZone(ZoneId.systemDefault()).toInstant()));
        notify.setFundBillList("[{“amount”:“15.00”,“fundChannel”:“ALIPAYACCOUNT”}]");
        notify.setVoucherDetailList(" [{“amount”:“0.20”,“merchantContribute”:“0.00”,“name”:“一键创建券模板的券名称”,“otherContribute”:“0.20”,“type”:“ALIPAY_DISCOUNT_VOUCHER”,“memo”:“学生卡8折优惠”]");
        notify.setPassbackParams("merchantBizType%3d3C%26merchantBizNo%3d2016010101111");

        int count = notifyMapper.insert(notify);
        assertThat(count, is(1));
    }

    @Test
    public void testSelectAllByOutTradeNo() {
        List<AlipayNotifyDO> notifies = notifyMapper.selectAllByOutTradeNo("6823789339978248");
        assertThat(notifies, not(empty()));
    }

}
