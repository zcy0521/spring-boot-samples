package com.sample.springboot.cache.redis.service.impl;

import com.sample.springboot.cache.redis.common.CachingNames;
import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.mapper.OrderMapper;
import com.sample.springboot.cache.redis.mapper.UserMapper;
import com.sample.springboot.cache.redis.query.OrderQuery;
import com.sample.springboot.cache.redis.service.OrderService;
import com.sample.springboot.cache.redis.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@CacheConfig(cacheNames = CachingNames.ORDER_CACHE)
public class OrderServiceImpl extends BaseService implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Cacheable
    public List<OrderDO> findAll() {
        List<OrderDO> orders = orderMapper.selectAll();
        ordersHandle(orders);
        return orders;
    }

    @Override
    public List<OrderDO> findAll(int pageNumber, int pageSize) {
        startPage(pageNumber, pageSize);
        List<OrderDO> orders = orderMapper.selectAll();
        ordersHandle(orders);
        return orders;
    }

    @Override
    public List<OrderDO> findAll(OrderQuery query) {
        Example example = Example.builder(OrderDO.class)
                .where(buildWhereSqls(query))
                .orderByDesc("gmt_create")
                .build();
        List<OrderDO> orders = orderMapper.selectByExample(example);
        ordersHandle(orders);
        return orders;
    }

    @Override
    public List<OrderDO> findAll(OrderQuery query, int pageNumber, int pageSize) {
        Example example = Example.builder(OrderDO.class)
                .where(buildWhereSqls(query))
                .orderByDesc("gmt_create")
                .build();
        startPage(pageNumber, pageSize);
        List<OrderDO> orders = orderMapper.selectByExample(example);
        ordersHandle(orders);
        return orders;
    }

    @Override
    public OrderDO findById(Long id) {
        if (null == id) {
            throw new IllegalArgumentException("ID must not be null!");
        }

        OrderDO order = orderMapper.selectByPrimaryKey(id);
        orderHandle(order);
        return order;
    }

    @Override
    public OrderDO findOne(OrderQuery query) {
        Example example = Example.builder(UserDO.class)
                .where(buildWhereSqls(query))
                .build();
        OrderDO order = orderMapper.selectOneByExample(example);
        orderHandle(order);
        return order;
    }

    @Override
    public Long insert(OrderDO entity) {
        if (null == entity) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        int rows = orderMapper.insertSelective(entity);
        if (rows > 0) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean update(OrderDO entity) {
        if (null == entity) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        if (null == entity.getId()) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        int rows = orderMapper.updateByPrimaryKeySelective(entity);
        return rows > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        if (null == id) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        int rows = orderMapper.deleteByPrimaryKey(id);
        return rows > 0;
    }

    /**
     * 根据查询对象生成查询条件
     *
     * @param query 查询对象
     * @return {@link WeekendSqls}
     */
    private WeekendSqls<OrderDO> buildWhereSqls(OrderQuery query) {
        WeekendSqls<OrderDO> sqls = WeekendSqls.custom();
        // 无查询条件
        if (null == query) {
            return sqls;
        }
        // 订单标题
        if(query.getSubject() != null){
            sqls.andEqualTo(OrderDO::getSubject, "%".concat(query.getSubject()).concat("%"));
        }
        // 最小金额
        if (query.getMinAmount() != null) {
            sqls.andGreaterThan(OrderDO::getTotalAmount, query.getMinAmount());
        }
        // 最大金额
        if (query.getMaxAmount() != null) {
            sqls.andLessThan(OrderDO::getTotalAmount, query.getMaxAmount());
        }
        // 所属用户
        if(query.getUserId() != null){
            sqls.andEqualTo(OrderDO::getUserId, query.getUserId());
        }
        return sqls;
    }

    /**
     * orders处理
     *
     * @param orders 订单集合
     */
    private void ordersHandle(List<OrderDO> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }

        // userIds
        Set<Long> userIds = orders.stream().map(OrderDO::getUserId).collect(Collectors.toSet());
        // 查询用户集合
        List<UserDO> users = userMapper.selectByIds(userIds);
        Map<Long, UserDO> userIdMap = users.stream().collect(Collectors.toMap(
                UserDO::getId,
                Function.identity(),
                (first, second) -> first
        ));
        orders.forEach(order -> order.setUser(userIdMap.get(order.getUserId())));
    }

    /**
     * order处理
     *
     * @param order 订单对象
     */
    private void orderHandle(OrderDO order) {
        if (null == order) {
            return;
        }

        // userId
        Long userId = order.getUserId();
        // 查询用户对象
        UserDO user = userMapper.selectByPrimaryKey(userId);
        order.setUser(user);
    }

}
