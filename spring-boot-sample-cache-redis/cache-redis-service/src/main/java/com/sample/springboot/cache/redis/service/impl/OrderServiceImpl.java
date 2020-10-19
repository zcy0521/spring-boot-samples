package com.sample.springboot.cache.redis.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.example.OrderExample;
import com.sample.springboot.cache.redis.mapper.OrderMapper;
import com.sample.springboot.cache.redis.query.OrderQuery;
import com.sample.springboot.cache.redis.service.OrderService;
import com.sample.springboot.cache.redis.service.UserService;
import com.sample.springboot.cache.redis.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<OrderDO> findAll() {
        return orderMapper.selectAll();
    }

    @Override
    public List<OrderDO> findAll(OrderQuery query, int number, int size) {
        if (query == null) {
            query = new OrderQuery();
        }

        // 查询条件
        OrderExample example = OrderExample.builder()
                .subject(query.getSubject())
                .minAmount(query.getMinAmount())
                .maxAmount(query.getMaxAmount())
                .userId(query.getUserId())
                .userIds(query.getUserIds())
                .deleted(query.getDeleted())
                .build();

        // 分页
        startPage(number, size);
        List<OrderDO> orders = orderMapper.selectAllByExample(example);

        // 处理订单关联信息
        ordersHandle(orders);
        return orders;
    }

    @Override
    public List<OrderDO> findAllByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }

        OrderExample example = OrderExample.builder()
                .userId(userId)
                .build();
        List<OrderDO> orders = orderMapper.selectAllByExample(example);

        // 处理订单关联信息
        ordersHandle(orders);
        return orders;
    }

    @Override
    public List<OrderDO> findAllByUserIds(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            throw new IllegalArgumentException();
        }

        List<OrderDO> orderList = Lists.newArrayList();

        Iterables.partition(userIds, PARTITION_SIZE).forEach(userIdList -> {
            Set<Long> _userIds = Sets.newHashSet(userIdList);
            OrderExample example = OrderExample.builder().userIds(_userIds).build();
            List<OrderDO> _orderList = orderMapper.selectAllByExample(example);
            orderList.addAll(_orderList);
        });

        // 处理订单关联信息
        ordersHandle(orderList);
        return orderList;
    }

    @Override
    public OrderDO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        OrderDO order = orderMapper.selectById(id);

        // 所属用户
        UserDO user = userService.findById(order.getUserId());
        order.setUser(user);

        return order;
    }

    @Override
    public Long insert(OrderDO entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        int count = orderMapper.insert(entity);
        return count > 0 ? entity.getId() : 0L;
    }

    @Override
    public Boolean update(OrderDO entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException();
        }

        int count = orderMapper.update(entity);
        return count > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        OrderDO order = orderMapper.selectById(id);
        if (order == null) {
            throw new IllegalArgumentException();
        }

        int count = orderMapper.deleteById(id);
        return count > 0;
    }

    @Override
    public int deleteByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            orderMapper.deleteByIds(_ids);
        });

        return ids.size();
    }

    @Override
    public int deleteByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }

        // 查询所属用户订单
        List<OrderDO> orders = findAllByUserId(userId);
        if (CollectionUtils.isEmpty(orders)) {
            return 0;
        }

        // 删除订单
        Set<Long> orderIds = orders.stream().map(OrderDO::getId).collect(Collectors.toSet());
        return deleteByIds(orderIds);
    }

    @Override
    public int deleteByUserIds(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            throw new IllegalArgumentException();
        }

        // 查询用户订单
        List<OrderDO> orders = findAllByUserIds(userIds);
        if (CollectionUtils.isEmpty(orders)) {
            return 0;
        }

        // 删除订单
        Set<Long> orderIds = orders.stream().map(OrderDO::getId).collect(Collectors.toSet());
        return deleteByIds(orderIds);
    }

    @Override
    public int deleteAll() {
        return orderMapper.deleteAll();
    }


    /**
     * orders处理
     */
    private void ordersHandle(List<OrderDO> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }

        // 用户集合
        Set<Long> userIds = orders.stream().map(OrderDO::getUserId).collect(Collectors.toSet());
        Map<Long, UserDO> userIdMap = userService.findIdMapByIds(userIds);

        orders.forEach(order -> order.setUser(userIdMap.get(order.getUserId())));
    }

}
