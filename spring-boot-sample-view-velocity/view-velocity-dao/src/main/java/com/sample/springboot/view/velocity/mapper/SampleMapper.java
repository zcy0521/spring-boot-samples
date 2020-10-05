package com.sample.springboot.view.velocity.mapper;

import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.example.SampleExample;
import com.sample.springboot.view.velocity.mapper.sql.SampleSQLProvider;
import com.sample.springboot.view.velocity.mybatis.scripting.ForeachDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface SampleMapper {


    /**
     * JDBC 要求，如果一个列允许使用 null 值，并且会使用值为 null 的参数，就必须要指定 JDBC 类型（jdbcType）
     * https://mybatis.org/mybatis-3/zh/sqlmap-xml.html
     */
    @Results(id="sampleResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="sample_integer", property="sampleInteger", jdbcType = JdbcType.INTEGER),
            @Result(column="sample_float", property="sampleFloat", jdbcType = JdbcType.REAL),
            @Result(column="sample_double", property="sampleDouble", jdbcType = JdbcType.DOUBLE),
            @Result(column="sample_string", property="sampleString", jdbcType = JdbcType.VARCHAR),
            @Result(column="sample_amount", property="sampleAmount", jdbcType = JdbcType.DECIMAL),
            @Result(column="sample_date", property="sampleDate", jdbcType = JdbcType.DATE),
            @Result(column="sample_date_time", property="sampleDateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="sample_enum", property="sampleEnum", jdbcType = JdbcType.INTEGER),
            @Result(column="disabled", property="disabled", jdbcType = JdbcType.BIT),
            @Result(column="sample_text", property="sampleText", jdbcType = JdbcType.LONGVARCHAR)
    })
    @SelectProvider(type = SampleSQLProvider.class, method = "selectAll")
    List<SampleDO> selectAll();

    @Lang(ForeachDriver.class)
    @ResultMap("sampleResult")
    @SelectProvider(type = SampleSQLProvider.class, method = "selectAllByIds")
    List<SampleDO> selectAllByIds(@Param("ids") Set<Long> ids);

    @Lang(ForeachDriver.class)
    @ResultMap("sampleResult")
    @SelectProvider(type = SampleSQLProvider.class, method = "selectAllByExample")
    List<SampleDO> selectAllByExample(SampleExample example);

    @ResultMap("sampleResult")
    @SelectProvider(type = SampleSQLProvider.class, method = "selectById")
    SampleDO selectById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @ResultMap("sampleResult")
    @SelectProvider(type = SampleSQLProvider.class, method = "selectOneByExample")
    SampleDO selectOneByExample(SampleExample example);

    @InsertProvider(type = SampleSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "`id`", keyProperty = "id", resultType = Long.class, before = false)
    int insert(SampleDO entity);

    @InsertProvider(type = SampleSQLProvider.class, method = "insertSelective")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "`id`", keyProperty = "id", resultType = Long.class, before = false)
    int insertSelective(SampleDO entity);

    @UpdateProvider(type = SampleSQLProvider.class, method = "update")
    int update(SampleDO entity);

    @UpdateProvider(type = SampleSQLProvider.class, method = "updateSelective")
    int updateSelective(SampleDO entity);

    @UpdateProvider(type = SampleSQLProvider.class, method = "disabledById")
    int disabledById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @UpdateProvider(type = SampleSQLProvider.class, method = "disabledByIds")
    int disabledByIds(@Param("ids") Set<Long> ids);

    @DeleteProvider(type = SampleSQLProvider.class, method = "deleteAll")
    int deleteAll();

    @DeleteProvider(type = SampleSQLProvider.class, method = "deleteById")
    int deleteById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @DeleteProvider(type = SampleSQLProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") Set<Long> ids);

    @SelectProvider(type = SampleSQLProvider.class, method = "countAll")
    int countAll();

    @Lang(ForeachDriver.class)
    @SelectProvider(type = SampleSQLProvider.class, method = "countByExample")
    int countByExample(SampleExample example);

}