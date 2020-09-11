package com.sample.springboot.data.redis.repository.sentinel;

import com.sample.springboot.data.redis.domain.sentinel.SentinelDO;
import org.springframework.data.repository.CrudRepository;

public interface SentinelRepository extends CrudRepository<SentinelDO, Long> {
}
