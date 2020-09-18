package com.sample.springboot.mapstruct.mapper;

import com.sample.springboot.mapstruct.domain.SampleDO;
import com.sample.springboot.mapstruct.mapper.decorator.SampleMapperDecorator;
import com.sample.springboot.mapstruct.mapper.enums.SampleEnumMapper;
import com.sample.springboot.mapstruct.vo.SampleVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {SampleEnumMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
@DecoratedWith(SampleMapperDecorator.class)
public interface SampleMapper {

    @Mapping(source = "sampleDate", target = "sampleDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "sampleTime", target = "sampleTime", dateFormat = "HH:mm")
    @Mapping(source = "sampleDatetime", target = "sampleDatetime", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(source = "sampleAmount", target = "sampleAmount", numberFormat = "#.00")
    SampleVO toVO(SampleDO sample);

    List<SampleVO> toVOs(List<SampleDO> samples);

    @InheritConfiguration(name = "toVO")
    void updateVOFromSample(SampleDO sample, @MappingTarget SampleVO sampleVO);

    @InheritInverseConfiguration(name = "toVO")
    @Mapping(target = "gmtCreate", ignore = true)
    @Mapping(target = "gmtModified", ignore = true)
    SampleDO fromVO(SampleVO sampleVO);

    List<SampleDO> fromVOs(List<SampleVO> sampleVOs);

    @InheritConfiguration(name = "fromVO")
    void updateSampleFromVO(SampleVO sampleVO, @MappingTarget SampleDO sample);

}
