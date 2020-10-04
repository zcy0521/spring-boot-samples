package com.sample.springboot.view.velocity.mapstruct.enums;

import com.sample.springboot.view.velocity.enums.SampleEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class SampleEnumMapper {

    /**
     * SampleEnum -> Integer
     */
    public Integer toValue(SampleEnum sampleEnum) {
        if (null == sampleEnum) {
            return null;
        }
        return sampleEnum.getValue();
    }

    /**
     * SampleEnum -> String
     */
    public String toLabel(SampleEnum sampleEnum) {
        if (null == sampleEnum) {
            return null;
        }
        return sampleEnum.getLabel();
    }

    /**
     * Integer -> SampleEnum
     */
    public SampleEnum fromValue(Integer value) {
        if (null == value) {
            return SampleEnum.ENUM_UNKNOWN;
        }

        SampleEnum sampleEnum = SampleEnum.of(value);
        if (null == sampleEnum) {
            throw new IllegalArgumentException(String.format("Cannot convert SampleEnum of value: %s", value));
        }
        return sampleEnum;
    }

}
