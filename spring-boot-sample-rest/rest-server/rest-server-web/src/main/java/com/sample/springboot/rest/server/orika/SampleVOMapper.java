package com.sample.springboot.rest.server.orika;

import com.sample.springboot.rest.server.domain.SampleDO;
import com.sample.springboot.rest.server.model.SampleVO;
import com.sample.springboot.rest.server.query.SampleQuery;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SampleVOMapper {

    @Autowired
    private MapperFactory mapperFactory;

    public SampleDO toSample(SampleVO sampleVO) {
        return mapperFactory.getMapperFacade().map(sampleVO, SampleDO.class);
    }

    public List<SampleDO> toSamples(List<SampleVO> sampleVOs) {
        return mapperFactory.getMapperFacade().mapAsList(sampleVOs, SampleDO.class);
    }

    public SampleVO fromSample(SampleDO sample) {
        return mapperFactory.getMapperFacade().map(sample, SampleVO.class);
    }

    public List<SampleVO> fromSamples(List<SampleDO> sampleList) {
        return mapperFactory.getMapperFacade().mapAsList(sampleList, SampleVO.class);
    }

    public SampleQuery toQuery(SampleVO.QueryVO queryVO) {
        return mapperFactory.getMapperFacade().map(queryVO, SampleQuery.class);
    }

}
