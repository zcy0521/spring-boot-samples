package com.sample.springboot.alipay.mapper;

import com.sample.springboot.alipay.domain.OrderDO;
import com.sample.springboot.alipay.example.OrderExample;
import com.sample.springboot.alipay.mapper.sql.OrderSQLProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface OrderMapper {

    @Results(id="orderResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="out_trade_no", property="outTradeNo", jdbcType = JdbcType.VARCHAR),
            @Result(column="trade_no", property="tradeNo", jdbcType = JdbcType.VARCHAR),
            @Result(column="total_amount", property="totalAmount", jdbcType = JdbcType.DECIMAL),
            @Result(column="app_id", property="appId", jdbcType = JdbcType.VARCHAR),
            @Result(column="seller_id", property="sellerId", jdbcType = JdbcType.VARCHAR),
            @Result(column="order_status", property="orderStatus", jdbcType = JdbcType.BIT),
            @Result(column="subject", property="subject", jdbcType = JdbcType.VARCHAR),
            @Result(column="body", property="body", jdbcType = JdbcType.VARCHAR),
            @Result(column="gmt_payment", property="gmtPayment", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_refund", property="gmtRefund", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_close", property="gmtClose", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="product_code", property="productCode", jdbcType = JdbcType.BIT),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT),
    })
    @SelectProvider(type = OrderSQLProvider.class, method = "selectAll")
    List<OrderDO> selectAll();

    @ResultMap("orderResult")
    @SelectProvider(type = OrderSQLProvider.class, method = "selectAllByExample")
    List<OrderDO> selectAllByExample(OrderExample example);

    @ResultMap("orderResult")
    @SelectProvider(type = OrderSQLProvider.class, method = "selectById")
    OrderDO selectById(@Param("id") Long id);

    @ResultMap("orderResult")
    @SelectProvider(type = OrderSQLProvider.class, method = "selectByOutTradeNo")
    OrderDO selectByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    @InsertProvider(type = OrderSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "`id`", keyProperty = "id", resultType = Long.class, before = false)
    int insert(OrderDO entity);

    @UpdateProvider(type = OrderSQLProvider.class, method = "updateSelective")
    int updateSelective(OrderDO entity);

    @DeleteProvider(type = OrderSQLProvider.class, method = "deleteAll")
    int deleteById(@Param("id") Long id);

    @DeleteProvider(type = OrderSQLProvider.class, method = "deleteAll")
    int deleteAll();

    @SelectProvider(type = OrderSQLProvider.class, method = "countAll")
    int countAll();

    @SelectProvider(type = OrderSQLProvider.class, method = "countByExample")
    int countByExample(OrderExample example);

}
