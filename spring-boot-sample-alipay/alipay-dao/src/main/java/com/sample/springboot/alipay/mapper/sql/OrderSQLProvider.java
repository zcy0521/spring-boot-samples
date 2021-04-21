package com.sample.springboot.alipay.mapper.sql;

import com.sample.springboot.alipay.domain.OrderDO;
import com.sample.springboot.alipay.example.OrderExample;
import com.sample.springboot.alipay.mybatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;

public class OrderSQLProvider {

    private static final String TABLE_NAME = "`alipay_order`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`total_amount`")
                .SELECT("`subject`")
                .SELECT("`body`")
                .SELECT("`order_status`")
                .SELECT("`gmt_payment`")
                .SELECT("`gmt_refund`")
                .SELECT("`gmt_close`")
                .SELECT("`product_code`")
                .FROM(TABLE_NAME)
                .WHERE_DELETED(false)
                .ORDER_BY("`id` ASC")
                .toString();
    }

    public String selectAllByExample(OrderExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`total_amount`")
                .SELECT("`subject`")
                .SELECT("`order_status`")
                .SELECT("`gmt_payment`")
                .SELECT("`gmt_refund`")
                .SELECT("`gmt_close`")
                .SELECT("`product_code`")
                .FROM(TABLE_NAME)
                .ORDER_BY("`id` DESC");

        if (example.getDeleted() != null) {
            sql.WHERE_DELETED(example.getDeleted());
        }
        return sql.toString();
    }

    public String selectById() {
        return new SQL()
                .SELECT_ALL()
                .FROM(TABLE_NAME)
                .WHERE_ID()
                .toString();
    }

    public String selectByOutTradeNo() {
        return new SQL()
                .SELECT_ALL()
                .FROM(TABLE_NAME)
                .WHERE("`out_trade_no` = #{outTradeNo}")
                .toString();
    }

    /**
     * JDBC 要求，如果一个列允许使用 null 值，并且会使用值为 null 的参数，就必须要指定 JDBC 类型（jdbcType）
     * https://mybatis.org/mybatis-3/zh/sqlmap-xml.html
     */
    public String insert() {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`out_trade_no`", "#{outTradeNo, jdbcType=VARCHAR}")
                .VALUES("`trade_no`", "#{tradeNo, jdbcType=VARCHAR}")
                .VALUES("`total_amount`", "#{totalAmount, jdbcType=DECIMAL}")
                .VALUES("`app_id`", "#{appId, jdbcType=VARCHAR}")
                .VALUES("`seller_id`", "#{sellerId, jdbcType=VARCHAR}")
                .VALUES("`order_status`", "#{orderStatus, jdbcType=BIT}")
                .VALUES("`subject`", "#{subject, jdbcType=VARCHAR}")
                .VALUES("`body`", "#{body, jdbcType=VARCHAR}")
                .VALUES("`gmt_payment`", "#{gmtPayment, jdbcType=TIMESTAMP}")
                .VALUES("`gmt_refund`", "#{gmtRefund, jdbcType=TIMESTAMP}")
                .VALUES("`gmt_close`", "#{gmtClose, jdbcType=TIMESTAMP}")
                .VALUES("`product_code`", "#{productCode, jdbcType=BIT}")
                .toString();
    }


    public String updateSelective(OrderDO entity) {
        SQL sql = new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID();

        // SET
        if (StringUtils.isNotBlank(entity.getOutTradeNo())) {
            sql.SET("`out_trade_no` = #{outTradeNo}");
        }
        if (StringUtils.isNotBlank(entity.getTradeNo())) {
            sql.SET("`trade_no` = #{tradeNo}");
        }
        if (null != entity.getTotalAmount()) {
            sql.SET("`total_amount` = #{totalAmount}");
        }
        if (StringUtils.isNotBlank(entity.getAppId())) {
            sql.SET("`app_id` = #{appId}");
        }
        if (StringUtils.isNotBlank(entity.getSellerId())) {
            sql.SET("`seller_id` = #{sellerId}");
        }
        if (null != entity.getOrderStatus()) {
            sql.SET("`order_status` = #{orderStatus}");
        }
        if (StringUtils.isNotBlank(entity.getSubject())) {
            sql.SET("`subject` = #{subject}");
        }
        if (StringUtils.isNotBlank(entity.getBody())) {
            sql.SET("`body` = #{body}");
        }
        if (null != entity.getGmtPayment()) {
            sql.SET("`gmt_payment` = #{gmtPayment}");
        }
        if (null != entity.getGmtRefund()) {
            sql.SET("`gmt_refund` = #{gmtRefund}");
        }
        if (null != entity.getGmtClose()) {
            sql.SET("`gmt_close` = #{gmtClose}");
        }
        if (null != entity.getProductCode()) {
            sql.SET("`product_code` = #{productCode}");
        }
        return sql.toString();
    }

    public String deleteById() {
        return new SQL()
                .DELETE_FROM(TABLE_NAME)
                .WHERE_ID()
                .toString();
    }

    public String deleteAll() {
        return "TRUNCATE TABLE " + TABLE_NAME;
    }

    public String countAll() {
        return new SQL()
                .COUNT()
                .FROM(TABLE_NAME)
                .toString();
    }

    public String countByExample(OrderExample example) {
        SQL sql = new SQL()
                .COUNT()
                .FROM(TABLE_NAME);

        // 查询条件
        if (example.getDeleted() != null) {
            sql.WHERE_DELETED(example.getDeleted());
        }
        return sql.toString();
    }

}
