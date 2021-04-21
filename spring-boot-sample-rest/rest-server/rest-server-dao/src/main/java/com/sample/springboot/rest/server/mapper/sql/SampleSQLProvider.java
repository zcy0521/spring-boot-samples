package com.sample.springboot.rest.server.mapper.sql;

import com.sample.springboot.rest.server.domain.SampleDO;
import com.sample.springboot.rest.server.example.SampleExample;
import com.sample.springboot.rest.server.mybatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class SampleSQLProvider {

    private static final String TABLE_NAME = "`rest_sample`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`sample_integer`")
                .SELECT("`sample_float`")
                .SELECT("`sample_double`")
                .SELECT("`sample_string`")
                .SELECT("`sample_amount`")
                .SELECT("`sample_date`")
                .SELECT("`sample_date_time`")
                .SELECT("`sample_enum`")
                .FROM(TABLE_NAME)
                .ORDER_BY("`id` ASC")
                .toString();
    }

    public String selectAllByExample(SampleExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`sample_integer`")
                .SELECT("`sample_float`")
                .SELECT("`sample_double`")
                .SELECT("`sample_string`")
                .SELECT("`sample_amount`")
                .SELECT("`sample_date`")
                .SELECT("`sample_date_time`")
                .SELECT("`sample_enum`")
                .FROM(TABLE_NAME)
                .ORDER_BY("`id` ASC");
        WHERE_EXAMPLE(sql, example);
        return sql.toString();
    }

    public String selectAllByIds(Set<Long> ids) {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`sample_integer`")
                .SELECT("`sample_float`")
                .SELECT("`sample_double`")
                .SELECT("`sample_string`")
                .SELECT("`sample_amount`")
                .SELECT("`sample_date`")
                .SELECT("`sample_date_time`")
                .SELECT("`sample_enum`")
                .FROM(TABLE_NAME)
                .WHERE_IDS(ids)
                .ORDER_BY("`id` ASC")
                .toString();
    }

    public String selectById(Long id) {
        return new SQL()
                .SELECT_ALL()
                .FROM(TABLE_NAME)
                .WHERE_ID()
                .toString();
    }

    public String selectOneByExample(SampleExample example) {
        SQL sql = new SQL()
                .SELECT("`id`")
                .SELECT("`sample_integer`")
                .SELECT("`sample_float`")
                .SELECT("`sample_double`")
                .SELECT("`sample_string`")
                .SELECT("`sample_amount`")
                .SELECT("`sample_date`")
                .SELECT("`sample_date_time`")
                .SELECT("`sample_enum`")
                .FROM(TABLE_NAME)
                .ORDER_BY("`id` ASC");
        WHERE_EXAMPLE(sql, example);
        return sql.toString();
    }

    /**
     * JDBC 要求，如果一个列允许使用 null 值，并且会使用值为 null 的参数，就必须要指定 JDBC 类型（jdbcType）
     * https://mybatis.org/mybatis-3/zh/sqlmap-xml.html
     */
    public String insert(SampleDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`sample_integer`", "#{sampleInteger, jdbcType=INTEGER}")
                .VALUES("`sample_float`", "#{sampleFloat, jdbcType=REAL}")
                .VALUES("`sample_double`", "#{sampleDouble, jdbcType=DOUBLE}")
                .VALUES("`sample_string`", "#{sampleString, jdbcType=VARCHAR}")
                .VALUES("`sample_amount`", "#{sampleAmount, jdbcType=DECIMAL}")
                .VALUES("`sample_date`", "#{sampleDate, jdbcType=DATE}")
                .VALUES("`sample_date_time`", "#{sampleDateTime, jdbcType=TIMESTAMP}")
                .VALUES("`sample_enum`", "#{sampleEnum, jdbcType=INTEGER}")
                .VALUES("`sample_text`", "#{sampleText, jdbcType=LONGVARCHAR}")
                .toString();
    }

    public String insertSelective(SampleDO entity) {
        SQL sql = new SQL()
                .INSERT_INTO(TABLE_NAME);

        // VALUES
        if (null != entity.getSampleInteger()) {
            sql.VALUES("`sample_integer`", "#{sampleInteger}");
        }
        if (null != entity.getSampleFloat()) {
            sql.VALUES("`sample_float`", "#{sampleFloat}");
        }
        if (null != entity.getSampleDouble()) {
            sql.VALUES("`sample_double`", "#{sampleDouble}");
        }
        if (StringUtils.isNotBlank(entity.getSampleString())) {
            sql.VALUES("`sample_string`", "#{sampleString}");
        }
        if (null != entity.getSampleAmount()) {
            sql.VALUES("`sample_amount`", "#{sampleAmount}");
        }
        if (null != entity.getSampleDate()) {
            sql.VALUES("`sample_date`", "#{sampleDate}");
        }
        if (null != entity.getSampleDateTime()) {
            sql.VALUES("`sample_date_time`", "#{sampleDateTime}");
        }
        if (null != entity.getSampleEnum()) {
            sql.VALUES("`sample_enum`", "#{sampleEnum}");
        }
        if (StringUtils.isNotBlank(entity.getSampleText())) {
            sql.VALUES("`sample_text`", "#{sampleText}");
        }
        return sql.toString();
    }

    /**
     * JDBC 要求，如果一个列允许使用 null 值，并且会使用值为 null 的参数，就必须要指定 JDBC 类型（jdbcType）
     * https://mybatis.org/mybatis-3/zh/sqlmap-xml.html
     */
    public String update(SampleDO entity) {
        return new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID()
                .SET("`sample_integer` = #{sampleInteger, jdbcType=INTEGER}")
                .SET("`sample_float` = #{sampleFloat, jdbcType=REAL}")
                .SET("`sample_double` = #{sampleDouble, jdbcType=DOUBLE}")
                .SET("`sample_string` = #{sampleString, jdbcType=VARCHAR}")
                .SET("`sample_amount` = #{sampleAmount, jdbcType=DECIMAL}")
                .SET("`sample_date` = #{sampleDate, jdbcType=DATE}")
                .SET("`sample_date_time` = #{sampleDateTime, jdbcType=TIMESTAMP}")
                .SET("`sample_enum` = #{sampleEnum, jdbcType=INTEGER}")
                .SET("`sample_text` = #{sampleText, jdbcType=LONGVARCHAR}")
                .toString();
    }

    public String updateSelective(SampleDO entity) {
        SQL sql = new SQL()
                .UPDATE(TABLE_NAME)
                .WHERE_ID();

        // SET
        if (null != entity.getSampleInteger()) {
            sql.SET("`sample_integer` = #{sampleInteger}");
        }
        if (null != entity.getSampleFloat()) {
            sql.SET("`sample_float` = #{sampleFloat}");
        }
        if (null != entity.getSampleDouble()) {
            sql.SET("`sample_double` = #{sampleDouble}");
        }
        if (StringUtils.isNotBlank(entity.getSampleString())) {
            sql.SET("`sample_string` = #{sampleString}");
        }
        if (null != entity.getSampleAmount()) {
            sql.SET("`sample_amount` = #{sampleAmount}");
        }
        if (null != entity.getSampleDate()) {
            sql.SET("`sample_date` = #{sampleDate}");
        }
        if (null != entity.getSampleDateTime()) {
            sql.SET("`sample_date_time` = #{sampleDateTime}");
        }
        if (null != entity.getSampleEnum()) {
            sql.SET("`sample_enum` = #{sampleEnum}");
        }
        if (StringUtils.isNotBlank(entity.getSampleText())) {
            sql.SET("`sample_text` = #{sampleText}");
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

    public String countByExample(SampleExample example) {
        SQL sql = new SQL()
                .COUNT()
                .FROM(TABLE_NAME);

        WHERE_EXAMPLE(sql, example);

        return sql.toString();
    }

    /**
     * 将SampleExample信息动态拼入SQL
     * IN查询需要@Lang处理SQL会被包裹<script></script> 因此 > >= < <= 需要包裹'<![CDATA[ conditions ]]'
     * @param sql SQL对象
     * @param example 查询条件
     */
    private void WHERE_EXAMPLE(SQL sql, SampleExample example) {
        if (null == example) {
            return;
        }
        if (null != example.getSampleInteger()) {
            sql.WHERE("`sample_integer` = #{sampleInteger}");
        }
        if (StringUtils.isNotBlank(example.getSampleString())) {
            sql.WHERE_LIKE("`sample_string`", "sampleString");
        }
        if (null != example.getMinAmount()) {
            sql.WHERE_CDATA("`sample_amount` >= #{minAmount}");
        }
        if (null != example.getMaxAmount()) {
            sql.WHERE_CDATA("`sample_amount` <= #{maxAmount}");
        }
        if (null != example.getMinDate()) {
            sql.WHERE_CDATA("`sample_date` >= #{minDate}");
        }
        if (null != example.getMaxDate()) {
            sql.WHERE_CDATA("`sample_date` <= #{maxDate}");
        }
        if (null != example.getMinDateTime()) {
            sql.WHERE_CDATA("`sample_date_time` >= #{minDateTime}");
        }
        if (null != example.getMaxDateTime()) {
            sql.WHERE_CDATA("`sample_date_time` <= #{maxDateTime}");
        }
        if (null != example.getSampleEnums() && example.getSampleEnums().length > 0) {
            sql.WHERE_IN("`sample_enum`", "sampleEnums.value");
        }
        sql.WHERE_DELETED(example.getDeleted());
    }

}
