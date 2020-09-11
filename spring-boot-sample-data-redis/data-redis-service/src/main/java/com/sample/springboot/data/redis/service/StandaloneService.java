package com.sample.springboot.data.redis.service;

import com.sample.springboot.data.redis.domain.standalone.StandaloneDO;

public interface StandaloneService {

    StandaloneDO save(StandaloneDO firstDO);

    StandaloneDO findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);

}
