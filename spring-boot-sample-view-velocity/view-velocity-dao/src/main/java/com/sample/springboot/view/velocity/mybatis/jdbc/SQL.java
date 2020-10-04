package com.sample.springboot.view.velocity.mybatis.jdbc;

import com.google.common.collect.Iterables;
import org.apache.ibatis.jdbc.AbstractSQL;

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
        if (null == ids || Iterables.isEmpty(ids)) {
            getSelf().WHERE("`id` IS NULL");
            return getSelf();
        }
        getSelf().WHERE("`id` IN (#{ids})");
        return getSelf();
    }

    public SQL INSERT_INTO(String tableName) {
        super.INSERT_INTO(tableName);
        getSelf().VALUES("`id`", "null");
        getSelf().VALUES("`gmt_create`", "NOW()");
        getSelf().VALUES("`disabled`", "0");
        return getSelf();
    }

    public SQL UPDATE(String tableName) {
        super.UPDATE(tableName);
        getSelf().SET("`gmt_modified` = NOW()");
        return getSelf();
    }

    public SQL DISABLED(String tableName) {
        super.UPDATE(tableName);
        getSelf().SET("`gmt_modified` = NOW()");
        getSelf().SET("`disabled` = 1");
        return getSelf();
    }

    public SQL COUNT() {
        getSelf().SELECT("COUNT(*)");
        return getSelf();
    }

}
