package com.sample.springboot.alipay.mapper.sql;

import com.sample.springboot.alipay.domain.AlipayNotifyDO;
import com.sample.springboot.alipay.mybatis.jdbc.SQL;

public class AlipayNotifySQLProvider {

    private static final String TABLE_NAME = "`alipay_notify`";

    public String selectAllByOutTradeNo() {
        return new SQL()
                .SELECT_ALL()
                .FROM(TABLE_NAME)
                .WHERE("`out_trade_no` = #{outTradeNo}")
                .toString();
    }

    public String insert(AlipayNotifyDO entity) {
        return new org.apache.ibatis.jdbc.SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`notify_id`", "#{notifyId, jdbcType=VARCHAR}")
                .VALUES("`notify_time`", "#{notifyTime, jdbcType=TIMESTAMP}")
                .VALUES("`notify_type`", "#{notifyType, jdbcType=VARCHAR}")
                .VALUES("`charset`", "#{charset, jdbcType=VARCHAR}")
                .VALUES("`version`", "#{version, jdbcType=VARCHAR}")
                .VALUES("`sign_type`", "#{signType, jdbcType=VARCHAR}")
                .VALUES("`sign`", "#{sign, jdbcType=VARCHAR}")
                .VALUES("`auth_app_id`", "#{authAppId, jdbcType=VARCHAR}")

                .VALUES("`trade_no`", "#{tradeNo, jdbcType=VARCHAR}")
                .VALUES("`app_id`", "#{appId, jdbcType=VARCHAR}")
                .VALUES("`out_trade_no`", "#{outTradeNo, jdbcType=VARCHAR}")
                .VALUES("`out_biz_no`", "#{outBizNo, jdbcType=VARCHAR}")
                .VALUES("`buyer_id`", "#{buyerId, jdbcType=VARCHAR}")
                .VALUES("`buyer_logon_id`", "#{buyerLogonId, jdbcType=VARCHAR}")
                .VALUES("`seller_id`", "#{sellerId, jdbcType=VARCHAR}")
                .VALUES("`seller_email`", "#{sellerEmail, jdbcType=VARCHAR}")
                .VALUES("`trade_status`", "#{tradeStatus, jdbcType=VARCHAR}")
                .VALUES("`total_amount`", "#{totalAmount, jdbcType=DECIMAL}")
                .VALUES("`receipt_amount`", "#{receiptAmount, jdbcType=DECIMAL}")
                .VALUES("`invoice_amount`", "#{invoiceAmount, jdbcType=DECIMAL}")
                .VALUES("`buyer_pay_amount`", "#{buyerPayAmount, jdbcType=DECIMAL}")
                .VALUES("`point_amount`", "#{pointAmount, jdbcType=DECIMAL}")
                .VALUES("`refund_fee`", "#{refundFee, jdbcType=DECIMAL}")
                .VALUES("`subject`", "#{subject, jdbcType=VARCHAR}")
                .VALUES("`body`", "#{body, jdbcType=VARCHAR}")
                .VALUES("`gmt_create`", "#{gmtCreate, jdbcType=TIMESTAMP}")
                .VALUES("`gmt_payment`", "#{gmtPayment, jdbcType=TIMESTAMP}")
                .VALUES("`gmt_refund`", "#{gmtRefund, jdbcType=TIMESTAMP}")
                .VALUES("`gmt_close`", "#{gmtClose, jdbcType=TIMESTAMP}")
                .VALUES("`fund_bill_list`", "#{fundBillList, jdbcType=VARCHAR}")
                .VALUES("`voucher_detail_list`", "#{voucherDetailList, jdbcType=VARCHAR}")
                .VALUES("`passback_params`", "#{passbackParams, jdbcType=VARCHAR}")
                .toString();
    }

}
