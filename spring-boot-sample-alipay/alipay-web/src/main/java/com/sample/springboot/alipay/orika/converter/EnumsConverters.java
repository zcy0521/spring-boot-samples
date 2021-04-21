package com.sample.springboot.alipay.orika.converter;

import com.sample.springboot.alipay.enums.AlipayProductCode;
import com.sample.springboot.alipay.enums.OrderStatus;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class EnumsConverters {

    public static class AlipayProductCodeToIntegerConverter extends BidirectionalConverter<AlipayProductCode, Integer> {

        /**
         * 后台返回页面
         * sampleEnum=null返回null 新增页面选择框不会被默认选中
         */
        @Override
        public Integer convertTo(AlipayProductCode source, Type<Integer> type) {
            if (null == source) {
                return null;
            }
            return source.value();
        }

        /**
         * 页面传值给后台
         * value=null设置为ENUM_UNKNOWN 保证数据库中`sample_enum`没有空值
         */
        @Override
        public AlipayProductCode convertFrom(Integer source, Type<AlipayProductCode> type) {
            if (null == source) {
                return AlipayProductCode.FAST_INSTANT_TRADE_PAY;
            }
            return AlipayProductCode.valueOf(source);
        }
    }

    public static class OrderStatusToIntegerConverter extends BidirectionalConverter<OrderStatus, Integer> {

        /**
         * 后台返回页面
         * sampleEnum=null返回null 新增页面选择框不会被默认选中
         */
        @Override
        public Integer convertTo(OrderStatus source, Type<Integer> type) {
            if (null == source) {
                return null;
            }
            return source.value();
        }

        /**
         * 页面传值给后台
         * value=null设置为ENUM_UNKNOWN 保证数据库中`sample_enum`没有空值
         */
        @Override
        public OrderStatus convertFrom(Integer source, Type<OrderStatus> type) {
            if (null == source) {
                return OrderStatus.ORDER_CREATE;
            }
            return OrderStatus.valueOf(source);
        }
    }

}
