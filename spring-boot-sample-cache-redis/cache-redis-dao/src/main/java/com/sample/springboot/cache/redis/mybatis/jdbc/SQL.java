package com.sample.springboot.cache.redis.mybatis.jdbc;

import com.google.common.collect.Iterables;
import org.apache.ibatis.jdbc.AbstractSQL;

import javax.annotation.Nullable;

public class SQL extends AbstractSQL<SQL> {

    @Override
    public SQL getSelf() {
        return this;
    }

    public SQL SELECT_ALL() {
        getSelf().SELECT("*");
        return getSelf();
    }

    public SQL WHERE_ID() {
        getSelf().WHERE("`id` = #{id}");
        return getSelf();
    }

    public SQL WHERE_IDS(Iterable ids) {
        if (ids == null || Iterables.isEmpty(ids)) {
            getSelf().WHERE("`id` IS NULL");
        } else {
            getSelf().WHERE("`id` IN (#{ids})");
        }
        return getSelf();
    }

    public SQL WHERE_IN(String column, String property, Iterable values) {
        if (values == null || Iterables.isEmpty(values)) {
            getSelf().WHERE(column + " IS NULL");
        } else {
            getSelf().WHERE(column + " IN (#{" + property + "})");
        }
        return getSelf();
    }

    public SQL WHERE_IN(String column, String property) {
        getSelf().WHERE(column + " IN (#{" + property + "})");
        return getSelf();
    }

    public SQL WHERE_LIKE(String column, String property) {
        getSelf().WHERE(column + " LIKE CONCAT('%', #{" + property +"}, '%')");
        return getSelf();
    }

    public SQL WHERE_CDATA(String conditions) {
        getSelf().WHERE("<![CDATA[ " + conditions + " ]]>");
        return getSelf();
    }

    public SQL WHERE_DELETED(@Nullable Boolean deleted) {
        if (null == deleted) {
            getSelf().WHERE("`is_deleted` = 0");
        } else {
            getSelf().WHERE("`is_deleted` = #{deleted}");
        }
        return getSelf();
    }

    public SQL INSERT_INTO(String tableName) {
        super.INSERT_INTO(tableName);
        getSelf().VALUES("`id`", "null");
        getSelf().VALUES("`gmt_create`", "NOW()");
        getSelf().VALUES("`is_deleted`", "0");
        return getSelf();
    }

    public SQL UPDATE(String tableName) {
        super.UPDATE(tableName);
        getSelf().SET("`gmt_modified` = NOW()");
        return getSelf();
    }

    public SQL DELETED(String tableName) {
        super.UPDATE(tableName);
        getSelf().SET("`gmt_modified` = NOW()");
        getSelf().SET("`is_deleted` = 1");
        return getSelf();
    }

    public SQL COUNT() {
        getSelf().SELECT("COUNT(*)");
        return getSelf();
    }

}
