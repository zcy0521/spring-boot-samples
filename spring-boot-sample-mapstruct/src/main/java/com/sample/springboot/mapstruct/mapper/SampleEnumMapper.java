package com.sample.springboot.mapstruct.mapper;

import com.sample.springboot.mapstruct.enums.SampleEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class SampleEnumMapper {

    /**
     * 后台返回页面
     * sampleEnum=null返回null 新增页面选择框不会被默认选中
     */
    public Integer convertTo(SampleEnum sampleEnum) {
        if (null == sampleEnum) {
            return null;
        }
        return sampleEnum.value();
    }

    /**
     * 页面传值给后台
     * value=null设置为ENUM_UNKNOWN 保证数据库中`sample_enum`没有空值
     */
    public SampleEnum convertFrom(Integer value) {
        if (null == value) {
            return SampleEnum.ENUM_UNKNOWN;
        }
        return SampleEnum.valueOf(value);
    }

}
