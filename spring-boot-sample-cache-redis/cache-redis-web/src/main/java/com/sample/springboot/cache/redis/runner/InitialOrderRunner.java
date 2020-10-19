package com.sample.springboot.cache.redis.runner;

import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.mapper.UserMapper;
import com.sample.springboot.cache.redis.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Order(4)
@Slf4j
@Component
@ConditionalOnProperty(prefix = "init-data", name = "order", havingValue = "true")
public class InitialOrderRunner implements ApplicationRunner {

    private static final int ORDER_NUM = 135;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("初始化订单信息...");

        // 删除订单
        orderService.deleteAll();

        // 用户ID
        Set<Long> userIds = userMapper.selectAll().stream().map(UserDO::getId).collect(Collectors.toSet());

        // 创建订单
        for (int i = 0; i < ORDER_NUM; i++) {
            OrderDO order = new OrderDO();
            order.setUserId(getRandomId(userIds));
            order.setSubject("订单" + (i + 1));
            order.setTotalAmount(new BigDecimal(RandomUtils.nextDouble(0.01d, 10000d)));
            orderService.insert(order);
        }

        log.info("初始化订单信息完成");
    }

    /**
     * 随机获取一个id
     * @param ids ID集合
     */
    private static Long getRandomId(Set<Long> ids) {
        Long[] idArray = ids.toArray(new Long[0]);
        int index = RandomUtils.nextInt(0, ids.size());
        return idArray[index];
    }

}
