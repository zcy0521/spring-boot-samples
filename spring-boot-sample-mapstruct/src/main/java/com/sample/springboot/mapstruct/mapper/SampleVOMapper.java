package com.sample.springboot.mapstruct.mapper;

import com.sample.springboot.mapstruct.domain.SampleDO;
import com.sample.springboot.mapstruct.mapper.decorator.SampleVOMapperDecorator;
import com.sample.springboot.mapstruct.mapper.enums.SampleEnumMapper;
import com.sample.springboot.mapstruct.vo.SampleVO;
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
    SampleDO toSample(SampleVO sampleVO);

    List<SampleDO> toSamples(List<SampleVO> sampleVOs);

    @InheritConfiguration(name = "toSample")
    void updateToSample(SampleVO source, @MappingTarget SampleDO target);

    @InheritInverseConfiguration(name = "toSample")
    SampleVO fromSample(SampleDO sample);

    List<SampleVO> fromSamples(List<SampleDO> samples);

    @InheritConfiguration(name = "fromSample")
    void updateFromSample(SampleDO source, @MappingTarget SampleVO target);

}
