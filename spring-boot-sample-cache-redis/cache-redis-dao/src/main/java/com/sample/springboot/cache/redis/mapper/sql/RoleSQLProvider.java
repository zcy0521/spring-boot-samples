package com.sample.springboot.cache.redis.mapper.sql;

import com.sample.springboot.cache.redis.domain.RoleDO;
import com.sample.springboot.cache.redis.mybatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class RoleSQLProvider {

    private static final String TABLE_NAME = "`cache_redis_role`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`role_name`")
                .FROM(TABLE_NAME)
                .toString();
    }

    public String selectAllByIds(Set<Long> ids) {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`role_name`")
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

    public String insert(RoleDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`role_name`", "#{roleName, jdbcType=VARCHAR}")
                .toString();
    }

    public String update(RoleDO entity) {
        SQL sql = new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID();

        // SET
        if (null != entity && StringUtils.isNotBlank(entity.getRoleName())) {
            sql.SET("`role_name` = #{roleName, jdbcType=VARCHAR}");
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

}
