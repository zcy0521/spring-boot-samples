package com.sample.springboot.alipay.enums;

import lombok.Getter;

@Getter
public enum OrderStatus implements Enums {

    ORDER_CREATE(1, "订单创建"),
    WAIT_BUYER_PAY(2, "付款中"),
    TRADE_SUCCESS(3, "支付成功"),
    TRADE_CLOSED(4, "交易关闭"),
    TRADE_FINISHED(5, "交易完结");

    private final int value;

    private final String label;

    OrderStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public int value() {
        return this.value;
    }

    public static OrderStatus valueOf(int value){
        OrderStatus orderStatus = resolve(value);
        if (orderStatus == null) {
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
        return orderStatus;
    }

    public static OrderStatus resolve(int value) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.value == value) {
                return orderStatus;
            }
        }
        return null;
    }
}
