package com.sample.springboot.mapstruct.mapper;

import com.sample.springboot.mapstruct.domain.SampleDO;
import com.sample.springboot.mapstruct.mapper.decorator.SampleVOMapStructMapperDecorator;
import com.sample.springboot.mapstruct.mapper.enums.SampleEnumMapper;
import com.sample.springboot.mapstruct.vo.SampleVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {SampleEnumMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
@DecoratedWith(SampleVOMapStructMapperDecorator.class)
public interface SampleVOMapStructMapper {

    @Mapping(source = "sampleDate", target = "sampleDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "sampleTime", target = "sampleTime", dateFormat = "HH:mm")
    @Mapping(source = "sampleDatetime", target = "sampleDatetime", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(source = "sampleAmount", target = "sampleAmount", numberFormat = "#.00")
    SampleVO from(SampleDO sample);

    List<SampleVO> listFrom(List<SampleDO> samples);

    @InheritConfiguration(name = "from")
    void updateFrom(SampleDO source, @MappingTarget SampleVO target);

    @InheritInverseConfiguration(name = "from")
    @Mapping(target = "gmtCreate", ignore = true)
    @Mapping(target = "gmtModified", ignore = true)
    SampleDO to(SampleVO sampleVO);

    List<SampleDO> listTo(List<SampleVO> sampleVOs);

    @InheritConfiguration(name = "to")
    void updateTo(SampleVO source, @MappingTarget SampleDO target);

}
