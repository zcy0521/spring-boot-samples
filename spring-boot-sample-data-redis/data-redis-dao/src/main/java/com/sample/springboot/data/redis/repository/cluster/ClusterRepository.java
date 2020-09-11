package com.sample.springboot.data.redis.repository.cluster;

import com.sample.springboot.data.redis.domain.cluster.ClusterDO;
import org.springframework.data.repository.CrudRepository;

public interface ClusterRepository extends CrudRepository<ClusterDO, Long> {
}
