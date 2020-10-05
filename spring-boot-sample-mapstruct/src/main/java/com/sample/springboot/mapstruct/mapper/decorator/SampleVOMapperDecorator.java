package com.sample.springboot.mapstruct.mapper.decorator;

import com.sample.springboot.mapstruct.domain.SampleDO;
import com.sample.springboot.mapstruct.mapper.SampleVOMapper;
import com.sample.springboot.mapstruct.vo.SampleVO;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SampleVOMapperDecorator implements SampleVOMapper {

    @Autowired
    private SampleVOMapper sampleVOMapper;

    @Override
    public SampleDO toSample(SampleVO sampleVO) {
        SampleDO sample = sampleVOMapper.toSample(sampleVO);
        // 自定义规则
        return sample;
    }

    @Override
    public SampleVO fromSample(SampleDO sample) {
        SampleVO sampleVO = sampleVOMapper.fromSample(sample);
        // 自定义规则
        return sampleVO;
    }

}
