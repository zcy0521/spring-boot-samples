package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.DeptAdminDO;
import com.sample.springboot.cache.redis.example.DeptAdminExample;
import com.sample.springboot.cache.redis.mapper.sql.DeptAdminSQLProvider;
import com.sample.springboot.cache.redis.mybatis.scripting.ForeachDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface DeptAdminMapper {

    @Results(id="deptAdminResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT),
            @Result(column="dept_id", property="deptId", jdbcType = JdbcType.BIGINT),
            @Result(column="user_id", property="userId", jdbcType = JdbcType.BIGINT),
            @Result(column="position", property="position", jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = DeptAdminSQLProvider.class, method = "selectAll")
    List<DeptAdminDO> selectAll();

    @Lang(ForeachDriver.class)
    @ResultMap("deptAdminResult")
    @SelectProvider(type = DeptAdminSQLProvider.class, method = "selectAllByExample")
    List<DeptAdminDO> selectAllByExample(DeptAdminExample example);

    @Lang(ForeachDriver.class)
    @ResultMap("deptAdminResult")
    @SelectProvider(type = DeptAdminSQLProvider.class, method = "selectOneByExample")
    DeptAdminDO selectOneByExample(DeptAdminExample example);

    @Lang(ForeachDriver.class)
    @ResultMap("deptAdminResult")
    @SelectProvider(type = DeptAdminSQLProvider.class, method = "selectAllByIds")
    List<DeptAdminDO> selectAllByIds(@Param("ids") Set<Long> ids);

    @ResultMap("deptAdminResult")
    @SelectProvider(type = DeptAdminSQLProvider.class, method = "selectById")
    DeptAdminDO selectById(@Param("id") Long id);

    @InsertProvider(type = DeptAdminSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false)
    int insert(DeptAdminDO entity);

    @UpdateProvider(type = DeptAdminSQLProvider.class, method = "deleteById")
    int deleteById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @UpdateProvider(type = DeptAdminSQLProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") Set<Long> ids);

    @DeleteProvider(type = DeptAdminSQLProvider.class, method = "deleteAll")
    int deleteAll();

}
