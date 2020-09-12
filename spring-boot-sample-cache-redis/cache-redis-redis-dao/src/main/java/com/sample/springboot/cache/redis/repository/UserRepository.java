package com.sample.springboot.cache.redis.repository;

import com.sample.springboot.cache.redis.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
