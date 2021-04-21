package com.sample.springboot.view.velocity.mapper.sql;

import com.sample.springboot.view.velocity.domain.FileDO;
import com.sample.springboot.view.velocity.mybatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class FileSQLProvider {

    private static final String TABLE_NAME = "`view_velocity_file`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`file_name`")
                .SELECT("`size`")
                .SELECT("`content_type`")
                .FROM(TABLE_NAME)
                .toString();
    }

    public String selectAllByIds(Set<Long> ids) {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`file_name`")
                .SELECT("`size`")
                .SELECT("`content_type`")
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

    public String insert(FileDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`file_name`", "#{fileName, jdbcType=VARCHAR}")
                .VALUES("`size`", "#{size, jdbcType=BIGINT}")
                .VALUES("`content_type`", "#{contentType, jdbcType=VARCHAR}")
                .toString();
    }

    public String updateSelective(FileDO entity) {
        SQL sql = new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID();

        // SET
        if (StringUtils.isNotBlank(entity.getFileName())) {
            sql.SET("`file_name` = #{fileName}");
        }
        if (entity.getSize() != null) {
            sql.SET("`size` = #{size}");
        }
        if (StringUtils.isNotBlank(entity.getContentType())) {
            sql.SET("`content_type` = #{contentType}");
        }
        return sql.toString();
    }

    public String deleteById(Long id) {
        return new SQL()
                .DELETED(TABLE_NAME)
                .WHERE_ID()
                .toString();
    }

    public String deleteByIds(Set<Long> ids) {
        return new SQL()
                .DELETED(TABLE_NAME)
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

}
