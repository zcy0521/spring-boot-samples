package com.sample.springboot.alipay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.sample.springboot.alipay.domain.AlipayConfigDO;
import com.sample.springboot.alipay.domain.AlipayNotifyDO;
import com.sample.springboot.alipay.mapper.AlipayNotifyMapper;
import com.sample.springboot.alipay.service.AlipayNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 异步通知接口
 *
 * 手机网站异步通知 https://opendocs.alipay.com/open/203/105286
 * APP支付异步通知 https://opendocs.alipay.com/open/204/105301
 * 电脑网站异步通知 https://opendocs.alipay.com/open/270/105902
 */
@Slf4j
@Service
public class AlipayNotifyServiceImpl implements AlipayNotifyService {

    @Autowired
    private Map<String, AlipayConfigDO> alipayConfigMap;

    @Autowired
    private Map<String, Config> alipayConfigMap2;

    @Autowired
    private AlipayNotifyMapper alipayNotifyMapper;

    @Override
    public boolean verify(Map<String, String> params) {
        if (CollectionUtils.isEmpty(params)) {
            throw new IllegalArgumentException();
        }

        // 获取支付宝Config
        String appId = params.get("app_id");
        AlipayConfigDO config = alipayConfigMap.get(appId);
        if (null == config) {
            log.error("调用 AlipaySignature.rsaCheckV1 异常，原因：APP_ID {} 获取AlipayConfig失败", appId);
            throw new RuntimeException();
        }

        // 验签
        try {
            String charset = params.get("charset");
            return AlipaySignature.rsaCheckV1(params, config.getAlipayPublicKey(), charset, config.getSignType());
        } catch (AlipayApiException e) {
            log.error("调用 AlipaySignature.rsaCheckV1 异常，原因：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Easy SDK 实现
     *
     * https://github.com/alipay/alipay-easysdk/blob/master/APIDoc.md#%E5%BC%82%E6%AD%A5%E9%80%9A%E7%9F%A5%E9%AA%8C%E7%AD%BE
     */
    @Override
    public boolean verify2(Map<String, String> params) {
        if (CollectionUtils.isEmpty(params)) {
            throw new IllegalArgumentException();
        }

        // 获取支付宝Config
        String appId = params.get("app_id");
        Config config = alipayConfigMap2.get(appId);
        if (null == config) {
            log.error("调用 AlipaySignature.rsaCheckV1 异常，原因：APP_ID {} 获取AlipayConfig失败", appId);
            throw new RuntimeException();
        }

        // 验签
        try {
            Factory.setOptions(config);
            return Factory.Payment.Common().verifyNotify(params);
        } catch (Exception e) {
            log.error("调用 Payment.Common.verifyNotify 异常，原因：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean save(AlipayNotifyDO notify) {
        if (null == notify) {
            throw new IllegalArgumentException();
        }

        int count = alipayNotifyMapper.insert(notify);
        return count > 0;
    }

}
