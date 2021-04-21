package com.sample.springboot.alipay.service;

import com.sample.springboot.alipay.domain.AlipayConfigDO;

import java.util.List;

public interface AlipayConfigService {

    List<AlipayConfigDO> findAll();

    boolean save(AlipayConfigDO entity);

    boolean update(AlipayConfigDO entity);

}
