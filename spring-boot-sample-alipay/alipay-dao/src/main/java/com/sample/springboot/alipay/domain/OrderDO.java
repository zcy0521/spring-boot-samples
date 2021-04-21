package com.sample.springboot.alipay.domain;

import com.sample.springboot.alipay.domain.base.BaseDO;
import com.sample.springboot.alipay.enums.AlipayProductCode;
import com.sample.springboot.alipay.enums.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderDO extends BaseDO {

    /**
     * 商户网站订单号，需保证商家系统中唯一。仅支持数字、字母、下划线。
     */
    private String outTradeNo;

    /**
     * 支付宝交易号。
     */
    private String tradeNo;

    /**
     * 订单金额。本次交易支付的订单金额，单位为人民币（元），精确到小数点后 2 位。
     */
    private BigDecimal totalAmount;

    /**
     * 开发者的 app_id。支付宝分配给开发者的应用 ID。
     */
    private String appId;

    /**
     * 卖家支付宝用户号。
     */
    private String sellerId;

    /**
     * 交易状态
     * 订单创建 ORDER_CREATE
     * 付款中   WAIT_BUYER_PAY
     * 支付成功 TRADE_SUCCESS
     * 交易关闭 TRADE_CLOSED
     * 交易完结 TRADE_FINISHED
     */
    private OrderStatus orderStatus;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 订单描述
     */
    private String body;

    /**
     * 交易付款时间。该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss。
     */
    private Date gmtPayment;

    /**
     * 交易退款时间。该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss.SSS。
     */
    private Date gmtRefund;

    /**
     * 交易结束时间。该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss。
     */
    private Date gmtClose;

    /**
     * 销售产品码
     * 电脑网站支付 FAST_INSTANT_TRADE_PAY
     * 手机网站支付 QUICK_WAP_PAY
     * 手机APP支付 QUICK_MSECURITY_PAY
     */
    private AlipayProductCode productCode;

}
