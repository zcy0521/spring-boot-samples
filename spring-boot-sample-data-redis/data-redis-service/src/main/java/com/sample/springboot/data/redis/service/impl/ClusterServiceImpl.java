package com.sample.springboot.data.redis.service.impl;

import com.sample.springboot.data.redis.domain.cluster.ClusterDO;
import com.sample.springboot.data.redis.repository.cluster.ClusterRepository;
import com.sample.springboot.data.redis.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private ClusterRepository clusterRepository;

    @Override
    public ClusterDO save(ClusterDO third) {
        return clusterRepository.save(third);
    }

    @Override
    public ClusterDO findById(Long id) {
        Optional<ClusterDO> entity = clusterRepository.findById(id);
        return entity.orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        clusterRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return clusterRepository.existsById(id);
    }

}
