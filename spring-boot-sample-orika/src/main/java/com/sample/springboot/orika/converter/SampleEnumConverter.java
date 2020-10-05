package com.sample.springboot.orika.converter;

import com.sample.springboot.orika.enums.SampleEnum;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class SampleEnumConverter extends BidirectionalConverter<SampleEnum, Integer> {

    /**
     * 后台返回页面
     * sampleEnum=null返回null 新增页面选择框不会被默认选中
     */
    @Override
    public Integer convertTo(SampleEnum source, Type<Integer> type) {
        if (null == source) {
            return null;
        }
        return source.value();
    }

    /**
     * 页面传值给后台
     * value=null设置为ENUM_UNKNOWN 保证数据库中`sample_enum`没有空值
     */
    @Override
    public SampleEnum convertFrom(Integer source, Type<SampleEnum> type) {
        if (null == source) {
            return SampleEnum.ENUM_UNKNOWN;
        }
        return SampleEnum.valueOf(source);
    }
}
