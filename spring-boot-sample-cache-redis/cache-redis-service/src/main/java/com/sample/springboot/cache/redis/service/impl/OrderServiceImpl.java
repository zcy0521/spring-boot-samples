package com.sample.springboot.cache.redis.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.example.OrderExample;
import com.sample.springboot.cache.redis.mapper.OrderMapper;
import com.sample.springboot.cache.redis.page.Page;
import com.sample.springboot.cache.redis.query.OrderQuery;
import com.sample.springboot.cache.redis.service.OrderService;
import com.sample.springboot.cache.redis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    /**
     * 超过100的数据查询分区
     */
    private static final int PARTITION_SIZE = 100;

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

        // 构建Page对象
        int totalElements = orderMapper.countByExample(example);
        Page page = Page.builder()
                .number(query.getNumber())
                .size(query.getSize())
                .totalElements(totalElements)
                .build();

        // 开启分页查询
        page.startPage();
        List<OrderDO> orderList = orderMapper.selectAllByExample(example);

        // 处理订单关联信息
        handleOrderList(orderList);
        // 将分页对象通过查询对象返回
        query.setPage(page);
        return orderList;
    }

    @Override
    public List<OrderDO> findAllByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        // 分区 IN 查询
        List<OrderDO> orderList = Lists.newArrayList();
        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            List<OrderDO> _orderList = orderMapper.selectAllByIds(_ids);
            orderList.addAll(_orderList);
        });

        // 处理订单关联信息
        handleOrderList(orderList);
        return orderList;
    }

    @Override
    public List<OrderDO> findAllByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }

        OrderExample example = OrderExample.builder()
                .userId(userId)
                .build();
        List<OrderDO> orderList = orderMapper.selectAllByExample(example);

        // 处理订单关联信息
        handleOrderList(orderList);
        return orderList;
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
        handleOrderList(orderList);
        return orderList;
    }

    @Override
    public OrderDO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        // 查询订单信息
        OrderDO order = orderMapper.selectById(id);

        // 用户
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
    public boolean update(OrderDO entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException();
        }

        int count = orderMapper.update(entity);
        return count > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        // 查询待删除订单是否存在
        OrderDO target = orderMapper.selectById(id);
        if (target == null) {
            return false;
        }

        // 删除订单
        int count = orderMapper.deleteById(id);
        return count > 0;
    }

    @Override
    public int deleteByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 查询待删除集合是否存在
        List<OrderDO> targetList = findAllByIds(ids);
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 待删除ID集合
        Set<Long> targetIds = targetList.stream().map(OrderDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(targetIds)) {
            return 0;
        }

        // 分区 IN 删除
        Iterables.partition(targetIds, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _targetIds = Sets.newHashSet(idList);
            orderMapper.deleteByIds(_targetIds);
        });
        return targetIds.size();
    }

    @Override
    public int deleteByUserId(Long userId) {
        if (userId == null) {
            return 0;
        }

        // 查询所属用户订单
        List<OrderDO> orderList = findAllByUserId(userId);
        if (CollectionUtils.isEmpty(orderList)) {
            return 0;
        }

        // 删除订单集合
        Set<Long> orderIds = orderList.stream().map(OrderDO::getId).collect(Collectors.toSet());
        return deleteByIds(orderIds);
    }

    @Override
    public int deleteByUserIds(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return 0;
        }

        // 查询用户订单
        List<OrderDO> orderList = findAllByUserIds(userIds);
        if (CollectionUtils.isEmpty(orderList)) {
            return 0;
        }

        // 删除订单集合
        Set<Long> orderIds = orderList.stream().map(OrderDO::getId).collect(Collectors.toSet());
        return deleteByIds(orderIds);
    }

    @Override
    public int deleteAll() {
        return orderMapper.deleteAll();
    }


    /**
     * 处理 orderList
     *
     * @param orderList 订单集合
     */
    private void handleOrderList(List<OrderDO> orderList) {
        if (CollectionUtils.isEmpty(orderList)) {
            return;
        }

        // 用户ID集合
        Set<Long> userIds = orderList.stream()
                .map(OrderDO::getUserId)
                .collect(Collectors.toSet());

        // 查询用户集合
        List<UserDO> userList = userService.findAllByIds(userIds);
        if (!CollectionUtils.isEmpty(userList)) {
            Map<Long, UserDO> userIdMap = userList.stream().collect(Collectors.toMap(
                    UserDO::getId,
                    Function.identity(),
                    (first, second) -> first
            ));
            orderList.forEach(order -> order.setUser(userIdMap.get(order.getUserId())));
        }
    }

}
