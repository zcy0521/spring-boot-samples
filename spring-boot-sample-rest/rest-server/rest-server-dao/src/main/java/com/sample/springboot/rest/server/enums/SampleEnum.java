package com.sample.springboot.rest.server.enums;

import lombok.Getter;

/**
 * 枚举示例
 */
@Getter
public enum SampleEnum implements Enums {

    ENUM_A(3, "枚举A"),
    ENUM_B(6, "枚举B"),
    ENUM_C(9, "枚举C"),
    ENUM_UNKNOWN(0, "枚举UNKNOWN");

    private final int value;

    private final String label;

    SampleEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public int value() {
        return this.value;
    }

    public static SampleEnum valueOf(int value){
        SampleEnum sampleEnum = resolve(value);
        if (sampleEnum == null) {
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
        return sampleEnum;
    }

    public static SampleEnum resolve(int value) {
        for (SampleEnum sampleEnum : values()) {
            if (sampleEnum.value == value) {
                return sampleEnum;
            }
        }
        return null;
    }
}
