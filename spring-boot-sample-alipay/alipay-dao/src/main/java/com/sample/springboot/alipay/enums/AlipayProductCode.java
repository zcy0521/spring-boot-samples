package com.sample.springboot.alipay.enums;

import lombok.Getter;

@Getter
public enum AlipayProductCode implements Enums {

    FAST_INSTANT_TRADE_PAY(1, "电脑网站支付"),
    QUICK_WAP_PAY(2, "手机网站支付"),
    QUICK_MSECURITY_PAY(3, "手机APP支付");

    private final int value;

    private final String label;

    AlipayProductCode(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public int value() {
        return this.value;
    }

    public static AlipayProductCode valueOf(int value){
        AlipayProductCode productCode = resolve(value);
        if (productCode == null) {
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
        return productCode;
    }

    public static AlipayProductCode resolve(int value) {
        for (AlipayProductCode productCode : values()) {
            if (productCode.value == value) {
                return productCode;
            }
        }
        return null;
    }
}
