package com.sample.springboot.cache.redis.mapper.sql;

import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.example.OrderExample;
import com.sample.springboot.cache.redis.mybatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class OrderSQLProvider {

    private static final String TABLE_NAME = "`cache_redis_order`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`subject`")
                .SELECT("`total_amount`")
                .SELECT("`user_id`")
                .FROM(TABLE_NAME)
                .toString();
    }

    public String selectAllByExample(OrderExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`subject`")
                .SELECT("`total_amount`")
                .SELECT("`user_id`")
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);
        return sql.toString();
    }

    public String selectAllByIds(Set<Long> ids) {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`subject`")
                .SELECT("`total_amount`")
                .SELECT("`user_id`")
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

    public String insert(OrderDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`subject`", "#{subject, jdbcType=VARCHAR}")
                .VALUES("`total_amount`", "#{totalAmount, jdbcType=DECIMAL}")
                .VALUES("`user_id`", "#{userId, jdbcType=BIGINT}")
                .toString();
    }

    public String update(OrderDO entity) {
        SQL sql = new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID();
        if (null != entity && StringUtils.isNotBlank(entity.getSubject())) {
            sql.SET("`subject` = #{subject, jdbcType=VARCHAR}");
        }
        if (null != entity && null != entity.getTotalAmount()) {
            sql.SET("`total_amount` = #{totalAmount, jdbcType=DECIMAL}");
        }
        if (null != entity && null != entity.getUserId()) {
            sql.SET("`user_id` = #{userId, jdbcType=BIGINT}");
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

    private void WHERE_EXAMPLE(SQL sql, OrderExample example) {
        if (example == null) {
            return;
        }

        if (StringUtils.isNotBlank(example.getSubject())) {
            sql.WHERE_LIKE("`subject`", "subject");
        }
        if (null != example.getMinAmount()) {
            sql.WHERE_CDATA("`total_amount` >= #{minAmount}");
        }
        if (null != example.getMaxAmount()) {
            sql.WHERE_CDATA("`total_amount` <= #{maxAmount}");
        }
        if (null != example.getUserId()) {
            sql.WHERE("`user_id` = #{userId}");
        }
        if (!CollectionUtils.isEmpty(example.getUserIds())) {
            sql.WHERE_IN("`user_id`", "userIds");
        }
        sql.WHERE_DELETED(example.getDeleted());
    }

}
