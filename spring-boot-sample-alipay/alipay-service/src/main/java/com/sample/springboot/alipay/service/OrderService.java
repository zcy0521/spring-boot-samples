package com.sample.springboot.alipay.service;

import com.sample.springboot.alipay.domain.AlipayNotifyDO;
import com.sample.springboot.alipay.domain.OrderDO;
import com.sample.springboot.alipay.query.OrderQuery;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    /**
     * 条件查询
     * @param query 携带查询信息
     * @param number 查询页数
     * @param size 查询数量
     */
    List<OrderDO> findAllByExample(@NonNull OrderQuery query, int number, int size);

    OrderDO findById(Long id);

    Long insert(OrderDO entity);

    boolean update(OrderDO entity);

    boolean deleteById(Long id);

    int deleteAll();

    OrderDO findByOutTradeNo(String outTradeNo);

    boolean updateByAlipayNotify(AlipayNotifyDO notify);

}
