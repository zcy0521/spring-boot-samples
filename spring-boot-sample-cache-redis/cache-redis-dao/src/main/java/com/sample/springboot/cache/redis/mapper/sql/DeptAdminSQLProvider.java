package com.sample.springboot.cache.redis.mapper.sql;

import com.sample.springboot.cache.redis.domain.DeptAdminDO;
import com.sample.springboot.cache.redis.example.DeptAdminExample;
import com.sample.springboot.cache.redis.mybatis.jdbc.SQL;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class DeptAdminSQLProvider {

    private static final String TABLE_NAME = "`cache_redis_dept_admin`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`dept_id`")
                .SELECT("`user_id`")
                .SELECT("`position`")
                .FROM(TABLE_NAME)
                .toString();
    }

    public String selectAllByExample(DeptAdminExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`dept_id`")
                .SELECT("`user_id`")
                .SELECT("`position`")
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);
        return sql.toString();
    }

    public String selectOneByExample(DeptAdminExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`dept_id`")
                .SELECT("`user_id`")
                .SELECT("`position`")
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);
        return sql.toString();
    }

    public String insert(DeptAdminDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`dept_id`", "#{deptId, jdbcType=BIGINT}")
                .VALUES("`user_id`", "#{userId, jdbcType=BIGINT}")
                .VALUES("`position`", "#{position, jdbcType=INTEGER}")
                .toString();
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

    private void WHERE_EXAMPLE(SQL sql, DeptAdminExample example) {
        if (example == null) {
            return;
        }

        if (example.getDeptId() != null) {
            sql.WHERE("`dept_id` = #{deptId}");
        }
        if (!CollectionUtils.isEmpty(example.getDeptIds())) {
            sql.WHERE_IN("`dept_id`", "deptIds");
        }
        if (example.getAdminId() != null) {
            sql.WHERE("`user_id` = #{adminId}");
        }
        if (!CollectionUtils.isEmpty(example.getAdminIds())) {
            sql.WHERE_IN("`user_id`", "adminIds");
        }
        if (example.getPosition() != null) {
            sql.WHERE("`position` = #{position.value}");
        }
        if (example.getPositions() != null) {
            sql.WHERE_IN("`position`", "positions.value");
        }
        sql.WHERE_DELETED(example.getDeleted());
    }

}
