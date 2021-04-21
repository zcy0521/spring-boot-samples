package com.sample.springboot.alipay.service.impl;

import com.sample.springboot.alipay.service.AlipayTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付宝支付接口
 *
 * 电脑网站支付API列表 https://opendocs.alipay.com/open/270/105900
 * 手机网站支付API列表 https://opendocs.alipay.com/open/203/105287
 * APP支付API列表 https://opendocs.alipay.com/open/204/105303
 */
@Slf4j
@Service("alipayTradeService")
public class AlipayTradeServiceImpl implements AlipayTradeService {

    /**
     * 电脑网站支付API
     * https://opendocs.alipay.com/apis/api_1/alipay.trade.page.pay
     */
    @Override
    public String pagePay(String outTradeNo) {
        return null;
    }

    /**
     * 手机网站支付API
     * https://opendocs.alipay.com/apis/api_1/alipay.trade.wap.pay
     */
    @Override
    public String wapPay(String outTradeNo) {
        return null;
    }

    /**
     * APP支付API
     * https://opendocs.alipay.com/apis/api_1/alipay.trade.app.pay
     */
    @Override
    public String appPay(String outTradeNo) {
        return null;
    }
}
