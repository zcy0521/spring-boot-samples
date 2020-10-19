package com.sample.springboot.cache.redis.mapper.sql;

import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.example.UserExample;
import com.sample.springboot.cache.redis.mybatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class UserSQLProvider {

    private static final String TABLE_NAME = "`cache_redis_user`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`user_name`")
                .SELECT("`dept_id`")
                .FROM(TABLE_NAME)
                .toString();
    }

    public String selectAllByExample(UserExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`user_name`")
                .SELECT("`dept_id`")
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);
        return sql.toString();
    }

    public String selectAllByIds(Set<Long> ids) {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`user_name`")
                .SELECT("`dept_id`")
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

    public String insert(UserDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`user_name`", "#{userName, jdbcType=VARCHAR}")
                .VALUES("`dept_id`", "#{deptId, jdbcType=BIGINT}")
                .toString();
    }

    public String update(UserDO entity) {
        SQL sql = new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID();

        // SET
        if (entity != null && StringUtils.isNotBlank(entity.getUserName())) {
            sql.SET("`user_name` = #{userName, jdbcType=VARCHAR}");
        }
        if (entity != null && entity.getDeptId() != null) {
            sql.SET("`dept_id` = #{deptId, jdbcType=BIGINT}");
        }
        return sql.toString();
    }

    public String updateByExample(UserDO entity, UserExample example) {
        SQL sql = new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID();

        // SET
        if (entity != null && StringUtils.isNotBlank(entity.getUserName())) {
            sql.SET("`user_name` = #{entity.userName, jdbcType=VARCHAR}");
        }
        if (entity != null && entity.getDeptId() != null) {
            sql.SET("`dept_id` = #{entity.deptId, jdbcType=BIGINT}");
        }

        // WHERE
        if (!CollectionUtils.isEmpty(example.getIds())) {
            sql.WHERE_IN("`ids`", "example.ids");
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

    public String countByExample(UserExample example) {
        SQL sql = new SQL()
                .COUNT()
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);
        return sql.toString();
    }

    private void WHERE_EXAMPLE(SQL sql, UserExample example) {
        if (example == null) {
            return;
        }

        if (!CollectionUtils.isEmpty(example.getIds())) {
            sql.WHERE_IN("`ids`", "ids");
        }
        if (StringUtils.isNotBlank(example.getUserName())) {
            sql.WHERE_LIKE("`user_name`", "userName");
        }
        if (example.getDeptId() != null) {
            sql.WHERE("`dept_id` = #{deptId}");
        }
        if (!CollectionUtils.isEmpty(example.getDeptIds())) {
            sql.WHERE_IN("`dept_id`", "deptId");
        }
        sql.WHERE_DELETED(example.getDeleted());
    }

}
