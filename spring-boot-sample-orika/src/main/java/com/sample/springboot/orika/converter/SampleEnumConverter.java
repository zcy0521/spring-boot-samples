package com.sample.springboot.orika.converter;

import com.sample.springboot.orika.enums.SampleEnum;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class SampleEnumConverter extends BidirectionalConverter<SampleEnum, Integer> {

    @Override
    public Integer convertTo(SampleEnum source, Type<Integer> type) {
        if (null == source) {
            return null;
        }
        return source.getValue();
    }

    @Override
    public SampleEnum convertFrom(Integer source, Type<SampleEnum> type) {
        if (null == source) {
            return null;
        }

        SampleEnum sampleEnum = SampleEnum.of(source);
        if (null == sampleEnum) {
            throw new IllegalArgumentException(String.format("Cannot convert SampleEnum of value: %s", source));
        }
        return sampleEnum;
    }
}
