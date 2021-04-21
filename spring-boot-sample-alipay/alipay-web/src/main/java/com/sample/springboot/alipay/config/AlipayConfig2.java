package com.sample.springboot.alipay.config;

import com.alipay.easysdk.kernel.Config;
import com.google.common.collect.Maps;
import com.sample.springboot.alipay.domain.AlipayConfigDO;
import com.sample.springboot.alipay.service.AlipayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class AlipayConfig2 {

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private AlipayConfigService alipayConfigService;

    @Bean
    public Map<String, Config> alipayConfg2() {
        // 支付宝配置Map
        Map<String, Config> configMap = Maps.newHashMap();

        // 支付宝配置
        List<AlipayConfigDO> alipayConfigs = alipayConfigService.findAll();
        for (AlipayConfigDO alipayConfig : alipayConfigs) {
            Config config = new Config();
            config.protocol = alipayProperties.getProtocol();
            config.gatewayHost = alipayProperties.getGatewayHost();
            config.signType = alipayProperties.getSignType();
            config.notifyUrl = alipayProperties.getNotifyUrl();
            config.appId = alipayConfig.getAppId();
            config.alipayPublicKey = alipayConfig.getAlipayPublicKey();
            config.merchantPrivateKey = alipayConfig.getMerchantPrivateKey();
            configMap.put(alipayConfig.getAppId(), config);
        }
        return configMap;
    }

}
