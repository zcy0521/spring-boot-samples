package com.sample.springboot.alipay.mapper;

import com.sample.springboot.alipay.domain.AlipayNotifyDO;
import com.sample.springboot.alipay.mapper.sql.AlipayNotifySQLProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface AlipayNotifyMapper {

    @Results(id="alipayNotifyResult", value={
            @Result(column="notify_id", property="notifyId", jdbcType = JdbcType.VARCHAR, id=true),
            @Result(column="notify_time", property="notifyTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="notify_type", property="notifyType", jdbcType = JdbcType.VARCHAR),
            @Result(column="charset", property="charset", jdbcType = JdbcType.VARCHAR),
            @Result(column="version", property="version", jdbcType = JdbcType.VARCHAR),
            @Result(column="sign_type", property="signType", jdbcType = JdbcType.VARCHAR),
            @Result(column="sign", property="sign", jdbcType = JdbcType.VARCHAR),
            @Result(column="auth_app_id", property="authAppId", jdbcType = JdbcType.VARCHAR),

            @Result(column="trade_no", property="tradeNo", jdbcType = JdbcType.VARCHAR),
            @Result(column="app_id", property="appId", jdbcType = JdbcType.VARCHAR),
            @Result(column="out_trade_no", property="outTradeNo", jdbcType = JdbcType.VARCHAR),
            @Result(column="out_biz_no", property="outBizNo", jdbcType = JdbcType.VARCHAR),
            @Result(column="buyer_id", property="buyerId", jdbcType = JdbcType.VARCHAR),
            @Result(column="buyer_logon_id", property="buyerLogonId", jdbcType = JdbcType.VARCHAR),
            @Result(column="seller_id", property="sellerId", jdbcType = JdbcType.VARCHAR),
            @Result(column="seller_email", property="sellerEmail", jdbcType = JdbcType.VARCHAR),
            @Result(column="trade_status", property="tradeStatus", jdbcType = JdbcType.VARCHAR),
            @Result(column="total_amount", property="totalAmount", jdbcType = JdbcType.DECIMAL),
            @Result(column="receipt_amount", property="receiptAmount", jdbcType = JdbcType.DECIMAL),
            @Result(column="invoice_amount", property="invoiceAmount", jdbcType = JdbcType.DECIMAL),
            @Result(column="buyer_pay_amount", property="buyerPayAmount", jdbcType = JdbcType.DECIMAL),
            @Result(column="point_amount", property="pointAmount", jdbcType = JdbcType.DECIMAL),
            @Result(column="refund_fee", property="refundFee", jdbcType = JdbcType.DECIMAL),
            @Result(column="subject", property="subject", jdbcType = JdbcType.VARCHAR),
            @Result(column="body", property="body", jdbcType = JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_payment", property="gmtPayment", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_refund", property="gmtRefund", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_close", property="gmtClose", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="fund_bill_list", property="fundBillList", jdbcType = JdbcType.VARCHAR),
            @Result(column="voucher_detail_list", property="voucherDetailList", jdbcType = JdbcType.VARCHAR),
            @Result(column="passback_params", property="passbackParams", jdbcType = JdbcType.VARCHAR)
    })
    @SelectProvider(type = AlipayNotifySQLProvider.class, method = "selectAllByOutTradeNo")
    List<AlipayNotifyDO> selectAllByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    @InsertProvider(type = AlipayNotifySQLProvider.class, method = "insert")
    int insert(AlipayNotifyDO entity);

}
