package com.sample.springboot.view.velocity.mapper;

import com.sample.springboot.view.velocity.domain.SampleFileDO;
import com.sample.springboot.view.velocity.example.SampleFileExample;
import com.sample.springboot.view.velocity.mapper.sql.SampleFileSQLProvider;
import com.sample.springboot.view.velocity.mybatis.scripting.ForeachDriver;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface SampleFileMapper {


    @Results(id="sampleFileResult", value={
            @Result(column="id", property="id", jdbcType = JdbcType.BIGINT, id=true),
            @Result(column="gmt_create", property="gmtCreate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType = JdbcType.TIMESTAMP),
            @Result(column="is_deleted", property="deleted", jdbcType = JdbcType.BIT),
            @Result(column="sample_id", property="sampleId", jdbcType = JdbcType.BIGINT),
            @Result(column="file_id", property="fileId", jdbcType = JdbcType.BIGINT),
            @Result(column="file_type", property="fileType", jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = SampleFileSQLProvider.class, method = "selectAll")
    List<SampleFileDO> selectAll();

    @Lang(ForeachDriver.class)
    @ResultMap("sampleFileResult")
    @SelectProvider(type = SampleFileSQLProvider.class, method = "selectAllByExample")
    List<SampleFileDO> selectAllByExample(SampleFileExample example);

    @Lang(ForeachDriver.class)
    @ResultMap("sampleFileResult")
    @SelectProvider(type = SampleFileSQLProvider.class, method = "selectOneByExample")
    SampleFileDO selectOneByExample(SampleFileExample example);

    @Lang(ForeachDriver.class)
    @ResultMap("sampleFileResult")
    @SelectProvider(type = SampleFileSQLProvider.class, method = "selectAllByIds")
    List<SampleFileDO> selectAllByIds(@Param("ids") Set<Long> ids);

    @ResultMap("sampleFileResult")
    @SelectProvider(type = SampleFileSQLProvider.class, method = "selectById")
    SampleFileDO selectById(@Param("id") Long id);

    @InsertProvider(type = SampleFileSQLProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "`id`", keyProperty = "id", resultType = Long.class, before = false)
    int insert(SampleFileDO entity);

    @UpdateProvider(type = SampleFileSQLProvider.class, method = "updateSelective")
    int updateSelective(SampleFileDO entity);

    @UpdateProvider(type = SampleFileSQLProvider.class, method = "deleteById")
    int deleteById(@Param("id") Long id);

    @Lang(ForeachDriver.class)
    @UpdateProvider(type = SampleFileSQLProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") Set<Long> ids);

    @DeleteProvider(type = SampleFileSQLProvider.class, method = "deleteAll")
    int deleteAll();

    @SelectProvider(type = SampleFileSQLProvider.class, method = "countAll")
    int countAll();

}