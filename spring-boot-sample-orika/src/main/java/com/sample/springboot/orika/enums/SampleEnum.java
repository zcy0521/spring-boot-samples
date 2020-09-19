package com.sample.springboot.orika.enums;

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

    public static SampleEnum of(Integer value){
        SampleEnum[] values = SampleEnum.values();
        for (SampleEnum sampleEnum : values) {
            if (sampleEnum.value.equals(value)) {
                return sampleEnum;
            }
        }
        return null;
    }
}
