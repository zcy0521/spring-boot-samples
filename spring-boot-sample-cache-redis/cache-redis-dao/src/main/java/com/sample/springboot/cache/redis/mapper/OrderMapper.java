package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.example.OrderExample;
import com.sample.springboot.cache.redis.mapper.sql.OrderSQLProvider;
import com.sample.springboot.cache.redis.mybatis.scripting.ForeachDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface OrderMapper {

    @Results(id="orderResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT),
            @Result(column="subject", property="subject", jdbcType = JdbcType.VARCHAR),
            @Result(column="total_amount", property="totalAmount", jdbcType = JdbcType.DECIMAL),
            @Result(column="user_id", property="userId", jdbcType = JdbcType.BIGINT)
    })
    @SelectProvider(type = OrderSQLProvider.class, method = "selectAll")
    List<OrderDO> selectAll();

    @Lang(ForeachDriver.class)
    @ResultMap("orderResult")
    @SelectProvider(type = OrderSQLProvider.class, method = "selectAllByExample")
    List<OrderDO> selectAllByExample(OrderExample example);

    @ResultMap("orderResult")
    @SelectProvider(type = OrderSQLProvider.class, method = "selectById")
    OrderDO selectById(@Param("id") Long id);

    @InsertProvider(type = OrderSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false)
    int insert(OrderDO entity);

    @UpdateProvider(type = OrderSQLProvider.class, method = "update")
    int update(OrderDO entity);

    @UpdateProvider(type = OrderSQLProvider.class, method = "deleteById")
    int deleteById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @UpdateProvider(type = OrderSQLProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") Set<Long> ids);

    @DeleteProvider(type = OrderSQLProvider.class, method = "deleteAll")
    int deleteAll();

}