package com.sample.springboot.view.velocity.enums;

/**
 * 枚举示例
 */
public enum SampleEnum implements Enums {

    ENUM_A(3, "枚举A"),
    ENUM_B(6, "枚举B"),
    ENUM_C(9, "枚举C"),
    ENUM_UNKNOWN(0, "枚举UNKNOWN");

    private Integer value;

    private String label;

    SampleEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public static SampleEnum of(Integer value){
        if (null == value) {
            return null;
        }

        SampleEnum[] values = SampleEnum.values();
        for (SampleEnum sampleEnum : values) {
            if (sampleEnum.value.equals(value)) {
                return sampleEnum;
            }
        }

        return SampleEnum.ENUM_UNKNOWN;
    }
}
