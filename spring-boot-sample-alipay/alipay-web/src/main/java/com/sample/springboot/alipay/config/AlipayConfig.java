package com.sample.springboot.alipay.config;

import com.google.common.collect.Maps;
import com.sample.springboot.alipay.domain.AlipayConfigDO;
import com.sample.springboot.alipay.service.AlipayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class AlipayConfig {

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private AlipayConfigService alipayConfigService;

    @Bean
    public Map<String, AlipayConfigDO> alipayConfg() {
        // DB中支付宝配置
        List<AlipayConfigDO> alipayConfigs = alipayConfigService.findAll();

        // 支付宝配置
        Map<String, AlipayConfigDO> configMap = Maps.newHashMap();
        for (AlipayConfigDO alipayConfig : alipayConfigs) {
            alipayConfig.setProtocol(alipayProperties.getProtocol());
            alipayConfig.setGatewayHost(alipayProperties.getGatewayHost());
            alipayConfig.setSignType(alipayProperties.getSignType());
            alipayConfig.setNotifyUrl(alipayProperties.getNotifyUrl());
            configMap.put(alipayConfig.getAppId(), alipayConfig);
        }
        return configMap;
    }

}
