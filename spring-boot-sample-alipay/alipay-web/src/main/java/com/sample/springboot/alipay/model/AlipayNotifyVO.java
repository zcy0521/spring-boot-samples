package com.sample.springboot.alipay.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 异步通知参数
 * 手机网站 https://opendocs.alipay.com/open/203/105286
 * APP支付 https://opendocs.alipay.com/open/204/105301
 * 电脑网站 https://opendocs.alipay.com/open/270/105902
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class AlipayNotifyVO {

    /**
     * 通知校验 ID。
     */
    private String notifyId;

    /**
     * 通知的发送时间。格式为 yyyy-MM-dd HH:mm:ss。
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date notifyTime;

    /**
     * 通知类型。
     */
    private String notifyType;

    /**
     * 编码格式。如 utf-8、gbk、gb2312 等。
     */
    private String charset;

    /**
     * 调用的接口版本。固定为：1.0。
     */
    private String version;

    /**
     * 签名类型。签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2。
     */
    private String signType;

    /**
     * 签名。
     */
    private String sign;

    /**
     * 授权方的 appid。由于本接口暂不开放第三方应用授权，因此 auth_app_id=app_id。
     */
    private String authAppId;




    /**
     * 支付宝交易号。支付宝交易凭证号。
     */
    private String tradeNo;

    /**
     * 开发者的 app_id。支付宝分配给开发者的应用 ID。
     */
    private String appId;

    /**
     * 商户订单号。原支付请求的商户订单号。
     */
    private String outTradeNo;

    /**
     * 商户业务号。商户业务 ID，主要是退款通知中返回退款申请的流水号。
     */
    private String outBizNo;

    /**
     * 买家支付宝用户号。买家支付宝账号对应的支付宝唯一用户号。以 2088 开头的纯 16 位数字。
     */
    private String buyerId;

    /**
     * 买家支付宝账号。
     */
    private String buyerLogonId;

    /**
     * 卖家支付宝用户号。
     */
    private String sellerId;

    /**
     * 卖家支付宝账号。
     */
    private String sellerEmail;

    /**
     * 交易状态。交易目前所处的状态。
     *
     * WAIT_BUYER_PAY 交易创建，等待买家付款。
     * TRADE_CLOSED   未付款交易超时关闭，或支付完成后全额退款。
     * TRADE_SUCCESS  交易支付成功。
     * TRADE_FINISHED 交易结束，不可退款。
     */
    private String tradeStatus;

    /**
     * 订单金额。本次交易支付的订单金额，单位为人民币（元），精确到小数点后 2 位。
     */
    private BigDecimal totalAmount;

    /**
     * 实收金额。商家在交易中实际收到的款项，单位为元，精确到小数点后 2 位。
     */
    private BigDecimal receiptAmount;

    /**
     * 开票金额。用户在交易中支付的可开发票的金额，单位为元，精确到小数点后 2 位。
     */
    private BigDecimal invoiceAmount;

    /**
     * 用户在交易中支付的金额，单位为元，精确到小数点付款金额。后 2 位。
     */
    private BigDecimal buyerPayAmount;

    /**
     * 集分宝金额。使用集分宝支付的金额，单位为元，精确到小数点后 2 位。
     */
    private BigDecimal pointAmount;

    /**
     * 总退款金额。退款通知中，返回总退款金额，单位为元，精确到小数点后 2 位。
     */
    private BigDecimal refundFee;

    /**
     * 订单标题。商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来。
     */
    private String subject;

    /**
     * 商品描述。该订单的备注、描述、明细等。对应请求时的 body 参数，原样通知回来。
     */
    private String body;

    /**
     * 交易创建时间。该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss。
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    /**
     * 交易付款时间。该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss。
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtPayment;

    /**
     * 交易退款时间。该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss.fff。
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private Date gmtRefund;

    /**
     * 交易结束时间。该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss。
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtClose;

    /**
     * 支付金额信息。支付成功的各个渠道金额信息。
     */
    private String fundBillList;

    /**
     * 优惠券信息。本交易支付时所使用的所有优惠券信息。
     */
    private String voucherDetailList;

    /**
     * 回传参数。公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回。本参数必须进行 UrlEncode 之后才可以发送给支付宝。
     */
    private String passbackParams;

}
