package com.sample.springboot.cache.redis.repository;

import com.sample.springboot.cache.redis.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
