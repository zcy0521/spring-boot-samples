package com.sample.springboot.data.jpa.enums;

import com.sample.springboot.data.jpa.converter.EnumsConverter;

/**
 * 枚举示例
 */
public enum SampleEnum implements Enums {

    ENUM_A(3),
    ENUM_B(6),
    ENUM_C(9),
    ENUM_UNKNOWN(0);

    private Integer value;

    SampleEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public static class Converter extends EnumsConverter<SampleEnum> {
        public Converter() {
            super(SampleEnum.class);
        }
    }

}
