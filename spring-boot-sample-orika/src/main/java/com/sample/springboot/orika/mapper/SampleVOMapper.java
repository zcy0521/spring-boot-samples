package com.sample.springboot.orika.mapper;

import com.sample.springboot.orika.domain.SampleDO;
import com.sample.springboot.orika.model.SampleVO;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SampleVOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public SampleVOMapper(MapperFactory mapperFactory) {
        mapperFactory.classMap(SampleDO.class, SampleVO.class)
                .byDefault()
                .customize(new CustomMapper<SampleDO, SampleVO>() {
                    @Override
                    public void mapAtoB(SampleDO source, SampleVO target, MappingContext context) {
                        // 自定义规则
                    }

                    @Override
                    public void mapBtoA(SampleVO source, SampleDO target, MappingContext context) {
                        // 自定义规则
                    }
                })
                .register();
        this.mapperFactory = mapperFactory;
    }

    public SampleDO convertTo(SampleVO sampleVO) {
        return mapperFactory.getMapperFacade().map(sampleVO, SampleDO.class);
    }

    public List<SampleDO> convertToList(List<SampleVO> sampleVOs) {
        return mapperFactory.getMapperFacade().mapAsList(sampleVOs, SampleDO.class);
    }

    public void updateTo(SampleVO source, SampleDO target) {
        mapperFactory.getMapperFacade().map(source, target);
    }

    public SampleVO convertFrom(SampleDO sample) {
        return mapperFactory.getMapperFacade().map(sample, SampleVO.class);
    }

    public List<SampleVO> convertFromList(List<SampleDO> samples) {
        return mapperFactory.getMapperFacade().mapAsList(samples, SampleVO.class);
    }

    public void updateFrom(SampleDO source, SampleVO target) {
        mapperFactory.getMapperFacade().map(source, target);
    }

}
