package com.sample.springboot.alipay.service.impl;

import com.sample.springboot.alipay.domain.AlipayNotifyDO;
import com.sample.springboot.alipay.domain.OrderDO;
import com.sample.springboot.alipay.enums.OrderStatus;
import com.sample.springboot.alipay.example.OrderExample;
import com.sample.springboot.alipay.mapper.OrderMapper;
import com.sample.springboot.alipay.page.Page;
import com.sample.springboot.alipay.query.OrderQuery;
import com.sample.springboot.alipay.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<OrderDO> findAllByExample(@NonNull OrderQuery query, int number, int size) {
        // 查询条件
        OrderExample example = OrderExample.builder()
                .orderStatus(query.getOrderStatus())
                .deleted(query.getDeleted())
                .build();

        // 分页信息
        int totalElements = orderMapper.countByExample(example);
        Page page = Page.builder()
                .number(number)
                .size(size)
                .totalElements(totalElements).build();

        // 开启分页查询
        page.startPage();
        List<OrderDO> orders = orderMapper.selectAllByExample(example);

        // 将分页对象通过查询对象返回
        query.setPage(page);
        return orders;
    }

    @Override
    public OrderDO findById(@NonNull Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public Long insert(@NonNull OrderDO entity) {
        int count = orderMapper.insert(entity);
        return count > 0 ? entity.getId() : 0L;
    }

    @Override
    public boolean update(@NonNull OrderDO entity) {
        if (null == entity.getId()) {
            throw new IllegalArgumentException();
        }

        // 查询待修改对象是否存在
        OrderDO order = orderMapper.selectById(entity.getId());
        if (order == null) {
            throw new IllegalArgumentException();
        }

        int count = orderMapper.updateSelective(entity);
        return count > 0;
    }

    @Override
    public boolean deleteById(@NonNull Long id) {
        // 查询待删除对象是否存在
        OrderDO target = orderMapper.selectById(id);
        if (target == null) {
            return false;
        }

        // 删除Sample
        int count = orderMapper.deleteById(id);
        return count > 0;
    }

    @Override
    public int deleteAll() {
        return orderMapper.deleteAll();
    }

    @Override
    public OrderDO findByOutTradeNo(@NonNull String outTradeNo) {
        OrderDO order = orderMapper.selectByOutTradeNo(outTradeNo);
        if (null == order) {
            log.error("查询订单失败，不存在订单，订单编号{}", outTradeNo);
        }

        return order;
    }

    @Override
    public boolean updateByAlipayNotify(@NonNull AlipayNotifyDO notify) {
        String outTradeNo = notify.getOutTradeNo();
        OrderDO order = orderMapper.selectByOutTradeNo(outTradeNo);

        // 订单不存在
        if (null == order) {
            log.error("支付宝异步通知更新订单状态失败，订单编号{}不存在", outTradeNo);
            return false;
        }

        // 校验通知金额
        BigDecimal totalAmount = notify.getTotalAmount();
        if (!totalAmount.equals(order.getTotalAmount())) {
            log.error("支付宝异步通知更新订单状态失败，订单金额有误，异步通知金额{}，订单实际金额{}", totalAmount, order.getTotalAmount());
            return false;
        }

        // 校验卖家ID
        String sellerId = notify.getSellerId();
        if (!StringUtils.equals(sellerId, order.getSellerId())) {
            log.error("支付宝异步通知更新订单状态失败，卖家ID有误，异步通知卖家ID{}，订单实际卖家ID{}", sellerId, order.getSellerId());
            return false;
        }

        // 校验卖家APPID
        String appId = notify.getAppId();
        if (!StringUtils.equals(appId, order.getAppId())) {
            log.error("支付宝异步通知更新订单状态失败，卖家APPID有误，异步通知卖家APPID{}，订单实际卖家APPID{}", appId, order.getAppId());
            return false;
        }

        // 过滤重复的通知结果数据
        OrderStatus orderStatus = order.getOrderStatus();
        if (!orderStatus.equals(OrderStatus.WAIT_BUYER_PAY)) {
            log.warn("支付宝异步通知更新订单状态失败，订单{}支付状态：{}", outTradeNo, orderStatus.getLabel());
            return true;
        }

        // 支付状态
        String tradeStatus = notify.getTradeStatus();
        // 支付宝ID
        String tradeNo = notify.getTradeNo();
        // 付款时间
        Date gmtPayment = notify.getGmtPayment();
        // 付款时间
        Date gmtClose = notify.getGmtClose();

        // 更新订单信息
        int count = 0;
        if (StringUtils.equals(tradeStatus, "TRADE_FINISHED")) {
            // 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            // 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            order.setTradeNo(tradeNo);
            order.setGmtPayment(gmtPayment);
            order.setOrderStatus(OrderStatus.TRADE_FINISHED);
            count = orderMapper.updateSelective(order);
        } else if (StringUtils.equals(tradeStatus, "TRADE_SUCCESS")) {
            // 如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            order.setTradeNo(tradeNo);
            order.setGmtPayment(gmtPayment);
            order.setOrderStatus(OrderStatus.TRADE_FINISHED);
            count = orderMapper.updateSelective(order);
        } else if (StringUtils.equals(tradeStatus, "TRADE_CLOSED")) {
            // 交易关闭后，支付宝系统发送该交易状态通知。
            order.setTradeNo(tradeNo);
            order.setGmtClose(gmtClose);
            order.setOrderStatus(OrderStatus.TRADE_FINISHED);
            count = orderMapper.updateSelective(order);
        }

        return count > 0;
    }
}
