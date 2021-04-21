package com.sample.springboot.alipay.mapper;

import com.sample.springboot.alipay.domain.AlipayConfigDO;
import com.sample.springboot.alipay.mapper.sql.AlipayConfigSQLProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface AlipayConfigMapper {

    @Results(id="alipayConfigResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="app_id", property="appId", jdbcType = JdbcType.VARCHAR),
            @Result(column="seller_id", property="sellerId", jdbcType = JdbcType.VARCHAR),
            @Result(column="alipay_public_key", property="alipayPublicKey", jdbcType = JdbcType.VARCHAR),
            @Result(column="merchant_private_key", property="merchantPrivateKey", jdbcType = JdbcType.VARCHAR),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT)
    })
    @SelectProvider(type = AlipayConfigSQLProvider.class, method = "selectAll")
    List<AlipayConfigDO> selectAll();

    @ResultMap("alipayConfigResult")
    @SelectProvider(type = AlipayConfigSQLProvider.class, method = "selectById")
    AlipayConfigDO selectById(@Param("id") Long id);

    @InsertProvider(type = AlipayConfigSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false)
    int insert(AlipayConfigDO entity);

    @UpdateProvider(type = AlipayConfigSQLProvider.class, method = "update")
    int update(AlipayConfigDO entity);

}
