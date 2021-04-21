package com.sample.springboot.alipay.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.app.models.AlipayTradeAppPayResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.sample.springboot.alipay.domain.OrderDO;
import com.sample.springboot.alipay.mapper.OrderMapper;
import com.sample.springboot.alipay.service.AlipayTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付宝支付接口 EasySDK实现
 *
 * EasySDK接口文档 https://github.com/alipay/alipay-easysdk/blob/master/APIDoc.md
 */
@Slf4j
@Service("alipayTradeService2")
public class AlipayTradeServiceImpl2 implements AlipayTradeService {

    private String returnUrl;

    @Autowired
    private Map<String, Config> alipayConfigMap;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 电脑网站 Page
     * https://github.com/alipay/alipay-easysdk/blob/master/APIDoc.md#%E7%94%B5%E8%84%91%E7%BD%91%E7%AB%99-page
     */
    @Override
    public String pagePay(@NonNull String outTradeNo) {
        OrderDO order = orderMapper.selectByOutTradeNo(outTradeNo);
        if (null == order) {
            log.error("调用Payment.Page.pay异常，原因：订单{}未查询到", outTradeNo);
            throw new RuntimeException();
        }

        // 获取alipayConfig
        String appId = order.getAppId();
        Config config = alipayConfigMap.get(appId);
        if (null == config) {
            log.error("调用Payment.Page.pay异常，原因：订单{}获取支付宝配置失败", outTradeNo);
            throw new RuntimeException();
        }

        Factory.setOptions(config);
        AlipayTradePagePayResponse response;
        try {
            response = Factory.Payment.Page()
                    .optional("body", order.getBody())
                    .pay(order.getSubject(), order.getOutTradeNo(), order.getTradeNo(), returnUrl);

            if (ResponseChecker.success(response)) {
                log.info("调用Payment.Page.pay成功");
            } else {
                log.error("调用Payment.Page.pay失败");
            }
        } catch (Exception e) {
            log.error("调用Payment.Page.pay异常，原因：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        return response.getBody();
    }

    /**
     * 手机网站 Wap
     * https://github.com/alipay/alipay-easysdk/blob/master/APIDoc.md#%E6%89%8B%E6%9C%BA%E7%BD%91%E7%AB%99-wap
     */
    @Override
    public String wapPay(String outTradeNo) {
        OrderDO order = orderMapper.selectByOutTradeNo(outTradeNo);
        if (null == order) {
            log.error("调用Payment.Wap.pay异常，原因：订单{}未查询到", outTradeNo);
            throw new RuntimeException();
        }

        // 获取alipayConfig
        String appId = order.getAppId();
        Config config = alipayConfigMap.get(appId);
        if (null == config) {
            log.error("调用Payment.Wap.pay异常，原因：订单{}获取支付宝配置失败", outTradeNo);
            throw new RuntimeException();
        }

        Factory.setOptions(config);
        AlipayTradeWapPayResponse response;
        try {
            response = Factory.Payment.Wap()
                    .optional("body", order.getBody())
                    .pay(order.getSubject(), order.getOutTradeNo(), order.getTradeNo(), config.notifyUrl, returnUrl);

            if (ResponseChecker.success(response)) {
                log.info("调用Payment.Wap.pay成功");
            } else {
                log.error("调用Payment.Wap.pay失败");
            }
        } catch (Exception e) {
            log.error("调用 Payment.Wap.pay 异常，原因：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        return response.getBody();
    }

    /**
     * App支付 App
     * https://github.com/alipay/alipay-easysdk/blob/master/APIDoc.md#app%E6%94%AF%E4%BB%98-app
     */
    @Override
    public String appPay(String outTradeNo) {
        OrderDO order = orderMapper.selectByOutTradeNo(outTradeNo);
        if (null == order) {
            log.error("调用Payment.App.pay异常，原因：订单{}未查询到", outTradeNo);
            throw new RuntimeException();
        }

        // 获取alipayConfig
        String appId = order.getAppId();
        Config config = alipayConfigMap.get(appId);
        if (null == config) {
            log.error("调用Payment.App.pay异常，原因：订单{}获取支付宝配置失败", outTradeNo);
            throw new RuntimeException();
        }

        Factory.setOptions(config);
        AlipayTradeAppPayResponse response;
        try {
            response = Factory.Payment.App()
                    .optional("body", order.getBody())
                    .pay(order.getSubject(), order.getOutTradeNo(), order.getTradeNo());

            if (ResponseChecker.success(response)) {
                log.info("调用Payment.App.pay成功");
            } else {
                log.error("调用Payment.App.pay失败");
            }
        } catch (Exception e) {
            log.error("调用Payment.App.pay异常，原因：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        return response.getBody();
    }

}
