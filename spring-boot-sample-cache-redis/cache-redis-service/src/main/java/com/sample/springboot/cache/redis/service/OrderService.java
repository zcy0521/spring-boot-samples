package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.query.OrderQuery;

import java.util.List;
import java.util.Set;

public interface OrderService {

    List<OrderDO> findAll();

    List<OrderDO> findAll(int pageNumber, int pageSize);

    List<OrderDO> findAll(OrderQuery query);

    List<OrderDO> findAll(OrderQuery query, int pageNumber, int pageSize);

    OrderDO findById(Long id);

    OrderDO findOne(OrderQuery query);

    Long insert(OrderDO entity);

    boolean update(OrderDO entity);

    boolean deleteById(Long id);

}
