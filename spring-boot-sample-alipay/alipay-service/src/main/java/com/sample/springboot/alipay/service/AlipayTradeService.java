package com.sample.springboot.alipay.service;

/**
 * 支付宝交易接口
 */
public interface AlipayTradeService {

    /**
     * 电脑网站支付
     *
     * @param outTradeNo 商户网站订单号，由商家自定义，需保证商家系统中唯一。仅支持数字、字母、下划线。
     *
     * @return tradeNo 该交易在支付宝系统中的交易流水号。最长64位。
     */
    String pagePay(String outTradeNo);

    /**
     * 手机网站支付接口2.0
     *
     * @param outTradeNo 商户网站订单号，由商家自定义，需保证商家系统中唯一。仅支持数字、字母、下划线。
     *
     * @return tradeNo 该交易在支付宝系统中的交易流水号。最长64位。
     */
    String wapPay(String outTradeNo);

    /**
     * app支付接口2.0
     *
     * @param outTradeNo 商户网站订单号，由商家自定义，需保证商家系统中唯一。仅支持数字、字母、下划线。
     *
     * @return tradeNo 该交易在支付宝系统中的交易流水号。最长64位。
     */
    String appPay(String outTradeNo);

//    /**
//     * 统一收单交易撤销接口
//     */
//    void cancel();
//
//    /**
//     * 统一收单交易关闭接口
//     */
//    void close();
//
//    /**
//     * 统一收单交易退款接口
//     */
//    void refund();
//
//    /**
//     * 统一收单交易退款接口
//     */
//    void refundQuery();
//
//    /**
//     * 查询对账单下载地址
//     *
//     * @param billType 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型，支持：
//     *                 trade：商户基于支付宝交易收单的业务账单；
//     *                 signcustomer：基于商户支付宝余额收入及支出等资金变动的帐务账单。
//     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，最早可下载2016年1月1日开始的日账单；月账单格式为yyyy-MM，最早可下载2016年1月开始的月账单。
//     *
//     * @return 账单下载地址
//     */
//    String billDownloadUrlQuery(String billType, String billDate);

}
