package com.sample.springboot.mapstruct.mapper;

import com.sample.springboot.mapstruct.decorator.SampleVOMapperDecorator;
import com.sample.springboot.mapstruct.domain.SampleDO;
import com.sample.springboot.mapstruct.model.SampleVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SampleEnumMapper.class}
)
@DecoratedWith(SampleVOMapperDecorator.class)
public interface SampleVOMapper {

    @Mapping(source = "sampleDate", target = "sampleDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "sampleDateTime", target = "sampleDateTime", dateFormat = "yyyy-MM-dd'T'HH:mm")
    @Mapping(target = "gmtCreate", ignore = true)
    @Mapping(target = "gmtModified", ignore = true)
    SampleDO convertTo(SampleVO vo);

    List<SampleDO> convertToList(List<SampleVO> voList);

    @InheritConfiguration(name = "convertTo")
    @Mapping(target = "gmtModified", ignore = true)
    @Mapping(target = "gmtCreate", ignore = true)
    void updateTo(SampleVO source, @MappingTarget SampleDO target);

    @InheritInverseConfiguration(name = "convertTo")
    SampleVO convertFrom(SampleDO sample);

    List<SampleVO> convertFromList(List<SampleDO> sampleList);

    @InheritConfiguration(name = "convertFrom")
    void updateFrom(SampleDO source, @MappingTarget SampleVO target);

}
