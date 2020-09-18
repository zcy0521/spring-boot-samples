package com.sample.springboot.mapstruct.mapper.enums;

import com.sample.springboot.mapstruct.enums.SampleEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class SampleEnumMapper {

    public Integer toInteger(SampleEnum sampleEnum) {
        if (null == sampleEnum) {
            return null;
        }
        return sampleEnum.getValue();
    }

    public SampleEnum fromInteger(Integer value) {
        if (null == value) {
            return null;
        }

        SampleEnum sampleEnum = SampleEnum.of(value);
        if (null == sampleEnum) {
            throw new IllegalArgumentException(String.format("Cannot convert SampleEnum of value: %s", value));
        }

        return sampleEnum;
    }

}
