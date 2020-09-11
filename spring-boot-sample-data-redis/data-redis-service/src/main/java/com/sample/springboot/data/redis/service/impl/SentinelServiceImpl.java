package com.sample.springboot.data.redis.service.impl;

import com.sample.springboot.data.redis.domain.sentinel.SentinelDO;
import com.sample.springboot.data.redis.repository.sentinel.SentinelRepository;
import com.sample.springboot.data.redis.service.SentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SentinelServiceImpl implements SentinelService {

    @Autowired
    private SentinelRepository sentinelRepository;

    @Override
    public SentinelDO save(SentinelDO second) {
        return sentinelRepository.save(second);
    }

    @Override
    public SentinelDO findById(Long id) {
        Optional<SentinelDO> entity = sentinelRepository.findById(id);
        return entity.orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        sentinelRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return sentinelRepository.existsById(id);
    }

}
