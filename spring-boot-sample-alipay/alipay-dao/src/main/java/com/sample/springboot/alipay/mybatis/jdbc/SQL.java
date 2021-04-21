package com.sample.springboot.alipay.mybatis.jdbc;

import com.google.common.collect.Iterables;
import org.apache.ibatis.jdbc.AbstractSQL;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class SQL extends AbstractSQL<SQL> {

    @Override
    public SQL getSelf() {
        return this;
    }

    public SQL SELECT_ALL() {
        getSelf().SELECT("*");
        return getSelf();
    }

    public SQL COUNT() {
        getSelf().SELECT("COUNT(*)");
        return getSelf();
    }

    public SQL WHERE_ID() {
        getSelf().WHERE("`id` = #{id}");
        return getSelf();
    }

    public SQL WHERE_IDS(@Nullable Iterable ids) {
        if (ids == null || Iterables.isEmpty(ids)) {
            getSelf().WHERE("`id` IS NULL");
        } else {
            getSelf().WHERE("`id` IN (#{ids})");
        }
        return getSelf();
    }

    public SQL WHERE_IN(@NonNull String column, @NonNull String property, @Nullable Iterable values) {
        if (values == null || Iterables.isEmpty(values)) {
            getSelf().WHERE(column + " IS NULL");
        } else {
            getSelf().WHERE(column + " IN (#{" + property + "})");
        }
        return getSelf();
    }

    public SQL WHERE_LIKE(@NonNull String column, @NonNull String property) {
        getSelf().WHERE(column + " LIKE CONCAT('%', #{" + property +"}, '%')");
        return getSelf();
    }

    public SQL WHERE_CDATA(@NonNull String conditions) {
        getSelf().WHERE("<![CDATA[ " + conditions + " ]]>");
        return getSelf();
    }

    public SQL WHERE_DELETED(@NonNull Boolean deleted) {
        if (deleted) {
            getSelf().WHERE("`is_deleted` = 1");
        } else {
            getSelf().WHERE("`is_deleted` = 0");
        }
        return getSelf();
    }

    public SQL INSERT_INTO(@NonNull String tableName) {
        super.INSERT_INTO(tableName);
        getSelf().VALUES("`is_deleted`", "0");
        return getSelf();
    }

    public SQL UPDATE(@NonNull String tableName) {
        super.UPDATE(tableName);
        return getSelf();
    }

    public SQL DELETE_FROM(@NonNull String tableName) {
        super.UPDATE(tableName);
        getSelf().SET("`is_deleted` = 1");
        return getSelf();
    }

}
