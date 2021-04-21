package com.sample.springboot.alipay.runner;

import com.sample.springboot.alipay.domain.AlipayConfigDO;
import com.sample.springboot.alipay.domain.OrderDO;
import com.sample.springboot.alipay.enums.OrderStatus;
import com.sample.springboot.alipay.service.AlipayConfigService;
import com.sample.springboot.alipay.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "init-data", name = "order", havingValue = "true")
public class InitialOrderRunner implements ApplicationRunner {

    private List<AlipayConfigDO> configs;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayConfigService alipayConfigService;

    @PostConstruct
    public void init() {
        this.configs = alipayConfigService.findAll();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 删除全部数据
        int count = orderService.deleteAll();
        log.warn("删除Sample记录 {} 条", count);

        // 插入120条记录
        int sampleNum = 120;
        for (int i = 1; i <= sampleNum; i++) {
            // 随机获取app_id seller_id
            String appId = getRandomAppId();
            String sellerId = getRandomSellerId(appId);

            // 生成订单信息
            OrderDO order = new OrderDO();
            order.setOutTradeNo(genOutTradeNo());
            order.setTotalAmount(new BigDecimal(RandomUtils.nextDouble(10d, 100d)));
            order.setAppId(appId);
            order.setSellerId(sellerId);
            order.setOrderStatus(OrderStatus.ORDER_CREATE);
            order.setSubject("测试交易" + i);
            order.setBody("测试交易内容" + i);
            orderService.insert(order);
        }

        log.info("数据初始化完成");
    }

    private String genOutTradeNo() {
        // 时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String ldt = LocalDateTime.now().format(formatter);
        // 三位随机数
        Random rnd = new Random();
        int n = 10000 + rnd.nextInt(90000);

        return ldt + n;
    }

    private String getRandomAppId() {
        List<String> appIdList = configs.stream().map(AlipayConfigDO::getAppId).collect(Collectors.toList());

        return getRandom(appIdList).orElseThrow(RuntimeException::new);
    }

    private String getRandomSellerId(String appId) {
        Map<String, AlipayConfigDO> configMap = configs.stream().collect(Collectors.toMap(AlipayConfigDO::getAppId, Function.identity()));
        AlipayConfigDO config = configMap.get(appId);
        Set<String> sellerIds = config.getSellerIds();

        return getRandom(sellerIds).orElseThrow(RuntimeException::new);
    }

    private static <E> Optional<E> getRandom(Collection<E> e) {
        return e.stream()
                .skip((int) (e.size() * Math.random()))
                .findFirst();
    }

}
