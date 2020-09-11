package com.sample.springboot.data.redis.repository.standalone;

import com.sample.springboot.data.redis.domain.standalone.StandaloneDO;
import org.springframework.data.repository.CrudRepository;

public interface StandaloneRepository extends CrudRepository<StandaloneDO, Long> {
}
