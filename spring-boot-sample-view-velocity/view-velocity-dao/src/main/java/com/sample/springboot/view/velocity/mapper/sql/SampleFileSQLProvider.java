package com.sample.springboot.view.velocity.mapper.sql;

import com.sample.springboot.view.velocity.domain.SampleFileDO;
import com.sample.springboot.view.velocity.example.SampleFileExample;
import com.sample.springboot.view.velocity.mybatis.jdbc.SQL;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class SampleFileSQLProvider {

    private static final String TABLE_NAME = "`view_velocity_sample_file`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`sample_id`")
                .SELECT("`file_id`")
                .SELECT("`file_type`")
                .FROM(TABLE_NAME)
                .toString();
    }

    public String selectAllByExample(SampleFileExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`sample_id`")
                .SELECT("`file_id`")
                .SELECT("`file_type`")
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);

        return sql.toString();
    }

    public String selectOneByExample(SampleFileExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`sample_id`")
                .SELECT("`file_id`")
                .SELECT("`file_type`")
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);

        return sql.toString();
    }

    public String selectAllByIds(Set<Long> ids) {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`sample_id`")
                .SELECT("`file_id`")
                .SELECT("`file_type`")
                .FROM(TABLE_NAME)
                .WHERE_IDS(ids)
                .toString();
    }

    public String selectById(Long id) {
        return new SQL()
                .SELECT_ALL()
                .FROM(TABLE_NAME)
                .WHERE_ID()
                .toString();
    }

    public String insert(SampleFileDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`sample_id`", "#{sampleId, jdbcType=BIGINT}")
                .VALUES("`file_id`", "#{fileId, jdbcType=BIGINT}")
                .VALUES("`file_type`", "#{fileType, jdbcType=INTEGER}")
                .toString();
    }

    public String updateSelective(SampleFileDO entity) {
        SQL sql = new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID();

        // SET
        if (null != entity.getSampleId()) {
            sql.SET("`sample_id` = #{sampleId}");
        }
        if (null != entity.getFileId()) {
            sql.SET("`file_id` = #{fileId}");
        }
        if (null != entity.getFileType()) {
            sql.SET("`file_type` = #{fileType}");
        }
        return sql.toString();
    }

    public String deleteById(Long id) {
        return new SQL()
                .DELETE_FROM(TABLE_NAME)
                .WHERE_ID()
                .toString();
    }

    public String deleteByIds(Set<Long> ids) {
        return new SQL()
                .DELETE_FROM(TABLE_NAME)
                .WHERE_IDS(ids)
                .toString();
    }

    public String deleteAll() {
        return "TRUNCATE TABLE " + TABLE_NAME;
    }

    public String countAll() {
        return new SQL()
                .COUNT()
                .FROM(TABLE_NAME)
                .toString();
    }

    private void WHERE_EXAMPLE(SQL sql, SampleFileExample example) {
        if (null == example) {
            return;
        }
        if (example.getSampleId() != null) {
            sql.WHERE("`sample_id` = #{sampleId}");
        }
        if (!CollectionUtils.isEmpty(example.getSampleIds())) {
            sql.WHERE_IN("`sample_id`","sampleIds");
        }
        if (example.getFileId() != null) {
            sql.WHERE("`file_id` = #{fileId}");
        }
        if (!CollectionUtils.isEmpty(example.getFileIds())) {
            sql.WHERE_IN("`file_id`","fileIds");
        }
        if (example.getFileType() != null) {
            sql.WHERE("`file_type` = #{fileType.value}");
        }
        if (example.getFileTypes() != null && example.getFileTypes().length > 0) {
            sql.WHERE_IN("`file_type`", "fileTypes.value");
        }
        sql.WHERE_DELETED(example.getDeleted());
    }

}
