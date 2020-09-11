package com.sample.springboot.data.redis.service;

import com.sample.springboot.data.redis.domain.sentinel.SentinelDO;

public interface SentinelService {

    SentinelDO save(SentinelDO second);

    SentinelDO findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);

}
