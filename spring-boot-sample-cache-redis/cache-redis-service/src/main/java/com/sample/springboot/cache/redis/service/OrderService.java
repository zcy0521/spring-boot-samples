package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.query.OrderQuery;

import java.util.List;
import java.util.Set;

public interface OrderService {

    List<OrderDO> findAll();

    List<OrderDO> findAll(OrderQuery query, int number, int size);

    List<OrderDO> findAllByUserId(Long userId);

    List<OrderDO> findAllByUserIds(Set<Long> userIds);

    OrderDO findById(Long id);

    Long insert(OrderDO entity);

    Boolean update(OrderDO entity);

    Boolean deleteById(Long id);

    int deleteByIds(Set<Long> ids);

    int deleteByUserId(Long userId);

    int deleteByUserIds(Set<Long> userIds);

    int deleteAll();

}
