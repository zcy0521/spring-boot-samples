package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.example.UserExample;
import com.sample.springboot.cache.redis.mapper.sql.UserSQLProvider;
import com.sample.springboot.cache.redis.mybatis.scripting.ForeachDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface UserMapper {

    @Results(id="userResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT),
            @Result(column="user_name", property="userName", jdbcType = JdbcType.VARCHAR),
            @Result(column="dept_id", property="deptId", jdbcType = JdbcType.BIGINT)
    })
    @SelectProvider(type = UserSQLProvider.class, method = "selectAll")
    List<UserDO> selectAll();

    @ResultMap("userResult")
    @SelectProvider(type = UserSQLProvider.class, method = "selectAllByExample")
    List<UserDO> selectAllByExample(UserExample example);

    @Lang(ForeachDriver.class)
    @ResultMap("userResult")
    @SelectProvider(type = UserSQLProvider.class, method = "selectAllByIds")
    List<UserDO> selectAllByIds(@Param("ids") Set<Long> ids);

    @ResultMap("userResult")
    @SelectProvider(type = UserSQLProvider.class, method = "selectById")
    UserDO selectById(@Param("id") Long id);

    @InsertProvider(type = UserSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false)
    int insert(UserDO entity);

    @UpdateProvider(type = UserSQLProvider.class, method = "update")
    int update(UserDO entity);

    @Lang(ForeachDriver.class)
    @UpdateProvider(type = UserSQLProvider.class, method = "updateByExample")
    int updateByExample(@Param("entity") UserDO entity, @Param("example") UserExample example);

    @UpdateProvider(type = UserSQLProvider.class, method = "deleteById")
    int deleteById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @UpdateProvider(type = UserSQLProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") Set<Long> ids);

    @DeleteProvider(type = UserSQLProvider.class, method = "deleteAll")
    int deleteAll();

    @Lang(ForeachDriver.class)
    @SelectProvider(type = UserSQLProvider.class, method = "countByExample")
    int countByExample(UserExample example);

}