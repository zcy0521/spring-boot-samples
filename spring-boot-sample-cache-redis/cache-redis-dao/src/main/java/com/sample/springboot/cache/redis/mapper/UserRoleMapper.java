package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.UserRoleDO;
import com.sample.springboot.cache.redis.example.UserRoleExample;
import com.sample.springboot.cache.redis.mapper.sql.UserRoleSQLProvider;
import com.sample.springboot.cache.redis.mybatis.scripting.ForeachDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface UserRoleMapper {

    @Results(id="userRoleResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT),
            @Result(column="user_id", property="userId", jdbcType = JdbcType.BIGINT),
            @Result(column="role_id", property="roleId", jdbcType = JdbcType.BIGINT)
    })
    @SelectProvider(type = UserRoleSQLProvider.class, method = "selectAll")
    List<UserRoleDO> selectAll();

    @Lang(ForeachDriver.class)
    @ResultMap("userRoleResult")
    @SelectProvider(type = UserRoleSQLProvider.class, method = "selectAllByExample")
    List<UserRoleDO> selectAllByExample(UserRoleExample example);

    @Lang(ForeachDriver.class)
    @ResultMap("userRoleResult")
    @SelectProvider(type = UserRoleSQLProvider.class, method = "selectOneByExample")
    UserRoleDO selectOneByExample(UserRoleExample example);

    @Lang(ForeachDriver.class)
    @ResultMap("userRoleResult")
    @SelectProvider(type = UserRoleSQLProvider.class, method = "selectAllByIds")
    List<UserRoleDO> selectAllByIds(@Param("ids") Set<Long> ids);

    @ResultMap("userRoleResult")
    @SelectProvider(type = UserRoleSQLProvider.class, method = "selectById")
    UserRoleDO selectById(@Param("id") Long id);

    @InsertProvider(type = UserRoleSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false)
    int insert(UserRoleDO entity);

    @DeleteProvider(type = UserRoleSQLProvider.class, method = "deleteById")
    int deleteById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @DeleteProvider(type = UserRoleSQLProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") Set<Long> ids);

    @DeleteProvider(type = UserRoleSQLProvider.class, method = "deleteAll")
    int deleteAll();

}