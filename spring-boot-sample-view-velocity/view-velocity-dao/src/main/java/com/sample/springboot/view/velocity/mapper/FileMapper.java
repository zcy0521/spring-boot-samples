package com.sample.springboot.view.velocity.mapper;

import com.sample.springboot.view.velocity.domain.FileDO;
import com.sample.springboot.view.velocity.mapper.sql.FileSQLProvider;
import com.sample.springboot.view.velocity.mybatis.scripting.ForeachDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface FileMapper {


    @Results(id="fileResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT),
            @Result(column="file_name", property="fileName", jdbcType = JdbcType.VARCHAR),
            @Result(column="size", property="size", jdbcType = JdbcType.BIGINT),
            @Result(column="content_type", property="contentType", jdbcType = JdbcType.VARCHAR)
    })
    @SelectProvider(type = FileSQLProvider.class, method = "selectAll")
    List<FileDO> selectAll();

    @Lang(ForeachDriver.class)
    @ResultMap("fileResult")
    @SelectProvider(type = FileSQLProvider.class, method = "selectAllByIds")
    List<FileDO> selectAllByIds(@Param("ids") Set<Long> ids);

    @ResultMap("fileResult")
    @SelectProvider(type = FileSQLProvider.class, method = "selectById")
    FileDO selectById(@Param("id") Long id);

    @InsertProvider(type = FileSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "`id`", keyProperty = "id", resultType = Long.class, before = false)
    int insert(FileDO entity);

    @UpdateProvider(type = FileSQLProvider.class, method = "updateSelective")
    int updateSelective(FileDO entity);

    @UpdateProvider(type = FileSQLProvider.class, method = "deleteById")
    int deleteById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @UpdateProvider(type = FileSQLProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") Set<Long> ids);

    @DeleteProvider(type = FileSQLProvider.class, method = "deleteAll")
    int deleteAll();

    @SelectProvider(type = FileSQLProvider.class, method = "countAll")
    int countAll();

}