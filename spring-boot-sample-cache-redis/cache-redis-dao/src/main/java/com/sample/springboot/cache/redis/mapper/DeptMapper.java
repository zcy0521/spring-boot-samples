package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.mapper.sql.DeptSQLProvider;
import com.sample.springboot.cache.redis.mybatis.scripting.ForeachDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface DeptMapper {

    @Results(id="deptResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT),
            @Result(column="parent_id", property="parentId", jdbcType = JdbcType.BIGINT),
            @Result(column="dept_name", property="deptName", jdbcType = JdbcType.VARCHAR),
            @Result(column="level", property="level", jdbcType = JdbcType.INTEGER),
            @Result(column="root_id", property="rootId", jdbcType = JdbcType.BIGINT),
            @Result(column="path", property="path", jdbcType = JdbcType.VARCHAR),
            @Result(column="leaf", property="leaf", jdbcType = JdbcType.BIT)
    })
    @SelectProvider(type = DeptSQLProvider.class, method = "selectAll")
    List<DeptDO> selectAll();

    @Lang(ForeachDriver.class)
    @ResultMap("deptResult")
    @SelectProvider(type = DeptSQLProvider.class, method = "selectAllByIds")
    List<DeptDO> selectAllByIds(@Param("ids") Set<Long> ids);

    @ResultMap("deptResult")
    @SelectProvider(type = DeptSQLProvider.class, method = "selectById")
    DeptDO selectById(@Param("id") Long id);

    @InsertProvider(type = DeptSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false)
    int insert(DeptDO entity);

    @UpdateProvider(type = DeptSQLProvider.class, method = "update")
    int update(DeptDO entity);

    @UpdateProvider(type = DeptSQLProvider.class, method = "deleteById")
    int deleteById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @UpdateProvider(type = DeptSQLProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") Set<Long> ids);

    @DeleteProvider(type = DeptSQLProvider.class, method = "deleteAll")
    int deleteAll();



    /**
     * 查询部门树ROOT节点
     */
    @ResultMap("deptResult")
    @SelectProvider(type = DeptSQLProvider.class, method = "selectTreeRoot")
    DeptDO selectTreeRoot();

    /**
     * 查询部门树
     */
    @ResultMap("deptResult")
    @SelectProvider(type = DeptSQLProvider.class, method = "selectTree")
    List<DeptDO> selectTree();

    /**
     * 查询祖先节点（含自己）
     */
    @ResultMap("deptResult")
    @SelectProvider(type = DeptSQLProvider.class, method = "selectAncestors")
    List<DeptDO> selectAncestors(@Param("id") Long id);

    /**
     * 查询子孙节点（不含自己）
     */
    @ResultMap("deptResult")
    @SelectProvider(type = DeptSQLProvider.class, method = "selectDescendants")
    List<DeptDO> selectDescendants(@Param("id") Long id);

    /**
     * 查询指定层级的子孙节点
     * level = 1 查询第一代后代
     * level = 2 查询第二代后代
     * ...
     */
    @ResultMap("deptResult")
    @SelectProvider(type = DeptSQLProvider.class, method = "selectDescendantsWithLevel")
    List<DeptDO> selectDescendantsWithLevel(@Param("id") Long id, @Param("level") int level);

}