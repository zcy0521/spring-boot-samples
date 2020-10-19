package com.sample.springboot.cache.redis.mapper.sql;

import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.mybatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class DeptSQLProvider {

    private static final String TABLE_NAME = "`cache_redis_dept`";

    /**
     * ROOT节点parentId
     */
    public static final Long ROOT_PARENT_ID = 0L;

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`parent_id`")
                .SELECT("`dept_name`")
                .FROM(TABLE_NAME)
                .toString();
    }

    public String selectAllByIds(Set<Long> ids) {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`parent_id`")
                .SELECT("`dept_name`")
                .FROM(TABLE_NAME)
                .WHERE_IDS(ids)
                .toString();
    }

    public String selectById(Long id) {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`parent_id`")
                .SELECT("`dept_name`")
                .FROM(TABLE_NAME)
                .WHERE_ID()
                .toString();
    }

    public String insert(DeptDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`parent_id`", "#{parentId, jdbcType=BIGINT}")
                .VALUES("`dept_name`", "#{deptName, jdbcType=VARCHAR}")
                .toString();
    }

    public String update(DeptDO entity) {
        SQL sql = new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID();

        // SET
        if (entity != null && entity.getParentId() != null) {
            sql.SET("`parent_id` = #{parentId, jdbcType=BIGINT}");
        }
        if (entity != null && StringUtils.isNotBlank(entity.getDeptName())) {
            sql.SET("`dept_name` = #{deptName, jdbcType=VARCHAR}");
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
        return new SQL()
                .DELETE_FROM(TABLE_NAME)
                .toString();
    }



    /**
     * 查询部门树ROOT节点
     */
    public String selectTreeRoot() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`parent_id`")
                .SELECT("`dept_name`")
                .FROM(TABLE_NAME)
                .WHERE("`parent_id` = " + ROOT_PARENT_ID)
                .toString();
    }

    /**
     * -- 查询树
     * WITH RECURSIVE t1(id, parent_id, level, path) AS (
     *   -- Anchor member.
     *   SELECT id, parent_id, 0 AS level, CAST(id AS CHAR(1000)) AS path FROM `cache_redis_dept` WHERE parent_id = 0
     *   UNION ALL
     *   -- Recursive member.
     *   SELECT t2.id, t2.parent_id, level+1, CONCAT(t1.path, ':', t2.id) AS path FROM `cache_redis_dept` t2, t1 WHERE t2.parent_id = t1.id
     * )
     * SELECT id, parent_id, level, path FROM t1;
     *
     * https://dev.mysql.com/doc/refman/8.0/en/with.html#common-table-expressions-recursive
     * https://oracle-base.com/articles/11g/recursive-subquery-factoring-11gr2
     */
    public String selectTree() {
        return "WITH RECURSIVE t1(id, parent_id, dept_name, level, path) AS (\n" +
                "  SELECT id, parent_id, dept_name, 0 AS level, CAST(id AS CHAR(1000)) AS path FROM `cache_redis_dept` WHERE parent_id = " + ROOT_PARENT_ID + "\n" +
                "  UNION ALL\n" +
                "  SELECT t2.id, t2.parent_id, t2.dept_name, level+1, CONCAT(t1.path, ':', t2.id) AS path FROM `cache_redis_dept` t2, t1 WHERE t2.parent_id = t1.id\n" +
                ")\n" +
                "SELECT id, parent_id, dept_name, level, path FROM t1";
    }

    /**
     * -- 查询全部父节点
     * WITH RECURSIVE t1(id, parent_id) AS (
     *   -- Anchor member.
     *   SELECT id, parent_id FROM `cache_redis_dept` WHERE id = ?
     *   UNION ALL
     *   -- Recursive member.
     *   SELECT t2.id, t2.parent_id FROM `cache_redis_dept` t2, t1 WHERE t2.id = t1.parent_id
     * )
     * SELECT id, parent_id FROM t1;
     *
     * https://dev.mysql.com/doc/refman/8.0/en/with.html#common-table-expressions-recursive
     * https://oracle-base.com/articles/11g/recursive-subquery-factoring-11gr2
     */
    public String selectAncestors(Long id) {
        return "WITH RECURSIVE t1(id, parent_id, dept_name) AS (\n" +
                "  SELECT id, parent_id, dept_name FROM `cache_redis_dept` WHERE id = #{id}\n" +
                "  UNION ALL\n" +
                "  SELECT t2.id, t2.parent_id, t2.dept_name FROM `cache_redis_dept` t2, t1 WHERE  t2.id = t1.parent_id\n" +
                ")\n" +
                "SELECT id, parent_id, dept_name FROM t1";
    }

    /**
     * -- 查询全部子节点（不含自己）深度优先
     * WITH RECURSIVE t1(id, parent_id, level, root_id, path) AS (
     *   -- Anchor member.
     *   SELECT id, parent_id, 1 AS level, id AS root_id, CAST(id AS CHAR(500)) AS path FROM `cache_redis_dept` WHERE parent_id = ?
     *   UNION ALL
     *   -- Recursive member.
     *   SELECT t2.id, t2.parent_id, level+1, t1.root_id, CONCAT(t1.path, '-', t2.id) AS path FROM `cache_redis_dept` t2, t1 WHERE t2.parent_id = t1.id
     * )
     * SELECT id, parent_id, level, root_id, path,
     *        CASE
     *          WHEN LEAD(level, 1, 1) OVER (ORDER BY path) <= level THEN 1
     *          ELSE 0
     *        END leaf
     * FROM t1
     * ORDER BY path;
     *
     * -- 查询全部子节点（不含自己）广度优先
     * WITH RECURSIVE t1(id, parent_id, level, root_id, path) AS (
     *   -- Anchor member.
     *   SELECT id, parent_id, 1 AS level, id AS root_id, CAST(id AS CHAR(500)) AS path FROM `cache_redis_dept` WHERE parent_id = ?
     *   UNION ALL
     *   -- Recursive member.
     *   SELECT t2.id, t2.parent_id, level+1, t1.root_id, CONCAT(t1.path, '-', t2.id) AS path FROM `cache_redis_dept` t2, t1 WHERE t2.parent_id = t1.id
     * )
     * SELECT id, parent_id, level, root_id, path,
     *        CASE
     *          WHEN LEAD(level, 1, 1) OVER (ORDER BY path) <= level THEN 1
     *          ELSE 0
     *        END leaf
     * FROM t1
     * ORDER BY level;
     *
     * https://dev.mysql.com/doc/refman/8.0/en/with.html#common-table-expressions-recursive
     * https://oracle-base.com/articles/11g/recursive-subquery-factoring-11gr2
     * http://mysqlserverteam.com/mysql-8-0-1-recursive-common-table-expressions-in-mysql-ctes-part-four-depth-first-or-breadth-first-traversal-transitive-closure-cycle-avoidance/
     */
    public String selectDescendants(Long id) {
        return "WITH RECURSIVE t1(id, parent_id, dept_name) AS (\n" +
                "  SELECT id, parent_id, dept_name FROM `cache_redis_dept` WHERE parent_id = #{id}\n" +
                "  UNION ALL\n" +
                "  SELECT t2.id, t2.parent_id, t2.dept_name FROM `cache_redis_dept` t2, t1 WHERE t2.parent_id = t1.id\n" +
                ")\n" +
                "SELECT id, parent_id, dept_name FROM t1";
    }

    public String selectDescendantsWithLevel(Long id, int level) {
        return "WITH RECURSIVE t1(id, parent_id, dept_name, level) AS (\n" +
                "  SELECT id, parent_id, dept_name, 1 AS level FROM `cache_redis_dept` WHERE parent_id = #{id}\n" +
                "  UNION ALL\n" +
                "  SELECT t2.id, t2.parent_id, t2.dept_name, level+1 FROM `cache_redis_dept` t2, t1 WHERE t2.parent_id = t1.id\n" +
                ")\n" +
                "SELECT id, parent_id, dept_name FROM t1\n" +
                "WHERE level = #{level}";
    }

}
