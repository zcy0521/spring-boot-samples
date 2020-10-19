package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.RoleDO;
import com.sample.springboot.cache.redis.mapper.sql.RoleSQLProvider;
import com.sample.springboot.cache.redis.mybatis.scripting.ForeachDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface RoleMapper {

    @Results(id="roleResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT),
            @Result(column="role_name", property="roleName", jdbcType = JdbcType.VARCHAR)
    })
    @SelectProvider(type = RoleSQLProvider.class, method = "selectAll")
    List<RoleDO> selectAll();

    @Lang(ForeachDriver.class)
    @ResultMap("roleResult")
    @SelectProvider(type = RoleSQLProvider.class, method = "selectAllByIds")
    List<RoleDO> selectAllByIds(@Param("ids") Set<Long> ids);

    @ResultMap("roleResult")
    @SelectProvider(type = RoleSQLProvider.class, method = "selectById")
    RoleDO selectById(@Param("id") Long id);

    @InsertProvider(type = RoleSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false)
    int insert(RoleDO entity);

    @UpdateProvider(type = RoleSQLProvider.class, method = "update")
    int update(RoleDO entity);

    @UpdateProvider(type = RoleSQLProvider.class, method = "deleteById")
    int deleteById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @UpdateProvider(type = RoleSQLProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") Set<Long> ids);

    @DeleteProvider(type = RoleSQLProvider.class, method = "deleteAll")
    int deleteAll();

}