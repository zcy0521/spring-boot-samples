package com.sample.springboot.data.redis.service;

import com.sample.springboot.data.redis.domain.cluster.ClusterDO;

public interface ClusterService {

    ClusterDO save(ClusterDO third);

    ClusterDO findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);

}
