package com.sample.springboot.alipay.service;

import com.sample.springboot.alipay.domain.AlipayNotifyDO;

import java.util.Map;

public interface AlipayNotifyService {

    boolean verify(Map<String, String> params);

    boolean verify2(Map<String, String> params);

    boolean save(AlipayNotifyDO notify);

}
