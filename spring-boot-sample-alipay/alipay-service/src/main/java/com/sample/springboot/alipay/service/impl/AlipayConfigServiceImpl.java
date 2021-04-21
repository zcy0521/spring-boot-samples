package com.sample.springboot.alipay.service.impl;

import com.sample.springboot.alipay.domain.AlipayConfigDO;
import com.sample.springboot.alipay.mapper.AlipayConfigMapper;
import com.sample.springboot.alipay.service.AlipayConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AlipayConfigServiceImpl implements AlipayConfigService {

    @Autowired
    private AlipayConfigMapper alipayConfigMapper;

    @Override
    public List<AlipayConfigDO> findAll() {
        return alipayConfigMapper.selectAll();
    }

    @Override
    public boolean save(AlipayConfigDO entity) {
        return false;
    }

    @Override
    public boolean update(AlipayConfigDO entity) {
        return false;
    }

}
