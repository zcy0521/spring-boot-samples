package com.sample.springboot.data.jpa.converter;

import com.sample.springboot.data.jpa.enums.Enums;

import javax.persistence.AttributeConverter;
import java.util.HashMap;
import java.util.Map;

public class EnumsConverter<E extends Enums> implements AttributeConverter<E, Integer> {

    private Class<E> type;

    private Map<Integer, E> enumMap;

    public EnumsConverter(Class<E> type) {
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
    public Integer convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public E convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        E anEnum = enumMap.get(dbData);
        if (null == anEnum) {
            throw new IllegalArgumentException("Cannot convert " + dbData + " to " + type.getSimpleName());
        }
        return anEnum;
    }

}
