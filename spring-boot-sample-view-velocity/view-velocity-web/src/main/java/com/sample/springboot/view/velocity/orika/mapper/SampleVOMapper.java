package com.sample.springboot.view.velocity.orika.mapper;

import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.model.SampleVO;
import com.sample.springboot.view.velocity.query.SampleQuery;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SampleVOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public SampleVOMapper(MapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
    }

    public SampleDO convertTo(SampleVO vo) {
        return mapperFactory.getMapperFacade().map(vo, SampleDO.class);
    }

    public List<SampleDO> convertToList(List<SampleVO> voList) {
        return mapperFactory.getMapperFacade().mapAsList(voList, SampleDO.class);
    }

    public void updateTo(SampleVO source, SampleDO target) {
        mapperFactory.getMapperFacade().map(source, target);
    }

    public SampleVO convertFrom(SampleDO sample) {
        return mapperFactory.getMapperFacade().map(sample, SampleVO.class);
    }

    public List<SampleVO> convertFromList(List<SampleDO> sampleList) {
        return mapperFactory.getMapperFacade().mapAsList(sampleList, SampleVO.class);
    }

    public void updateFrom(SampleDO source, SampleVO target) {
        mapperFactory.getMapperFacade().map(source, target);
    }

    public SampleQuery convertTo(SampleVO.Query query) {
        return mapperFactory.getMapperFacade().map(query, SampleQuery.class);
    }

}
