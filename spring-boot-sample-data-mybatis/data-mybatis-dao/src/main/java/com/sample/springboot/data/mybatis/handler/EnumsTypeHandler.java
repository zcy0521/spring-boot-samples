package com.sample.springboot.data.mybatis.handler;

import com.sample.springboot.data.mybatis.enums.Enums;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Enums处理
 * {@link org.apache.ibatis.type.EnumOrdinalTypeHandler}
 */
@MappedTypes(Enums.class)
public class EnumsTypeHandler<E extends Enums> extends BaseTypeHandler<E> {

    private Class<E> type;

    private Map<Integer, E> enumMap;

    public EnumsTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;

        E[] enums = type.getEnumConstants();
        if (enums != null) {
            this.enumMap = new HashMap<>(enums.length);
            for (E anEnum : enums) {
                this.enumMap.put(anEnum.getValue(), anEnum);
            }
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 根据columnName在结果集中获取结果
        int columnValue = rs.getInt(columnName);
        if (columnValue == 0 && rs.wasNull()) {
            return null;
        }
        return getEnum(columnValue);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 根据columnIndex在结果集中获取结果
        int columnValue = rs.getInt(columnIndex);
        if (columnValue == 0 && rs.wasNull()) {
            return null;
        }
        return getEnum(columnValue);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 根据columnIndex在结果集中获取结果
        int columnValue = cs.getInt(columnIndex);
        if (columnValue == 0 && cs.wasNull()) {
            return null;
        }
        return getEnum(columnValue);
    }

    /**
     * 根据value获取Enum
     * @param value 枚举值
     */
    private E getEnum(int value) {
        E anEnum = enumMap.get(value);
        if (null == anEnum) {
            throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getSimpleName());
        }
        return anEnum;
    }

}
