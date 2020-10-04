package com.sample.springboot.view.velocity.mapstruct.vo;

import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.mapstruct.enums.SampleEnumMapper;
import com.sample.springboot.view.velocity.query.SampleQuery;
import com.sample.springboot.view.velocity.vo.SampleVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SampleEnumMapper.class}
)
public interface SampleVOMapper {

    @Mapping(source = "sampleDate", target = "sampleDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "sampleDateTime", target = "sampleDateTime", dateFormat = "yyyy-MM-dd'T'HH:mm")
    @Mapping(target = "gmtCreate", ignore = true)
    @Mapping(target = "gmtModified", ignore = true)
    SampleDO toSample(SampleVO sampleVO);

    List<SampleDO> toSamples(List<SampleVO> sampleVOs);

    @InheritConfiguration(name = "toSample")
    void updateToSample(SampleVO sampleVO, @MappingTarget SampleDO sample);

    @InheritInverseConfiguration(name = "toSample")
    @Mapping(target = "ids", ignore = true)
    @Mapping(target = "query", ignore = true)
    SampleVO fromSample(SampleDO sample);

    List<SampleVO> fromSamples(List<SampleDO> samples);

    @InheritConfiguration(name = "fromSample")
    void updateFromSample(SampleDO sample, @MappingTarget SampleVO sampleVO);


    @Mapping(source = "minDate", target = "minDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "maxDate", target = "maxDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "minDateTime", target = "minDateTime", dateFormat = "yyyy-MM-dd'T'HH:mm")
    @Mapping(source = "maxDateTime", target = "maxDateTime", dateFormat = "yyyy-MM-dd'T'HH:mm")
    @Mapping(target = "page", ignore = true)
    SampleQuery toSampleQuery(SampleVO.Query query);

}
