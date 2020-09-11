package com.sample.springboot.data.redis.service.impl;

import com.sample.springboot.data.redis.domain.standalone.StandaloneDO;
import com.sample.springboot.data.redis.repository.standalone.StandaloneRepository;
import com.sample.springboot.data.redis.service.StandaloneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StandaloneServiceImpl implements StandaloneService {

    @Autowired
    private StandaloneRepository standaloneRepository;

    @Override
    public StandaloneDO save(StandaloneDO firstDO) {
        return standaloneRepository.save(firstDO);
    }

    @Override
    public StandaloneDO findById(Long id) {
        Optional<StandaloneDO> entity = standaloneRepository.findById(id);
        return entity.orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        standaloneRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return standaloneRepository.existsById(id);
    }

}
