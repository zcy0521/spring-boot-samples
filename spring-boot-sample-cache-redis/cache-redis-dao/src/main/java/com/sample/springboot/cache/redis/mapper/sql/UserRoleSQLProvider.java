package com.sample.springboot.cache.redis.mapper.sql;

import com.sample.springboot.cache.redis.domain.UserRoleDO;
import com.sample.springboot.cache.redis.example.UserRoleExample;
import com.sample.springboot.cache.redis.mybatis.jdbc.SQL;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class UserRoleSQLProvider {

    private static final String TABLE_NAME = "`cache_redis_user_role`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`user_id`")
                .SELECT("`role_id`")
                .FROM(TABLE_NAME)
                .toString();
    }

    public String selectAllByExample(UserRoleExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`user_id`")
                .SELECT("`role_id`")
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);
        return sql.toString();
    }

    public String selectOneByExample(UserRoleExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`user_id`")
                .SELECT("`role_id`")
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);
        return sql.toString();
    }

    public String insert(UserRoleDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`user_id`", "#{userId, jdbcType=BIGINT}")
                .VALUES("`role_id`", "#{roleId, jdbcType=BIGINT}")
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

    private void WHERE_EXAMPLE(SQL sql, UserRoleExample example) {
        if (example == null) {
            return;
        }

        if (example.getUserId() != null) {
            sql.WHERE("`user_id` = #{userId}");
        }
        if (!CollectionUtils.isEmpty(example.getUserIds())) {
            sql.WHERE_IN("`user_id`", "userIds");
        }
        if (example.getRoleId() != null) {
            sql.WHERE("`role_id` = #{roleId}");
        }
        if (!CollectionUtils.isEmpty(example.getRoleIds())) {
            sql.WHERE_IN("`role_id`", "roleIds");
        }
        sql.WHERE_DELETED(example.getDeleted());
    }

}
