package com.sample.springboot.mapstruct.mapper.decorator;

import com.sample.springboot.mapstruct.domain.SampleDO;
import com.sample.springboot.mapstruct.mapper.SampleVOMapper;
import com.sample.springboot.mapstruct.vo.SampleVO;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SampleVOMapperDecorator implements SampleVOMapper {

    @Autowired
    private SampleVOMapper sampleVOMapper;

    @Override
    public SampleVO from(SampleDO sample) {
        SampleVO sampleVO = sampleVOMapper.from(sample);
        // 自定义规则
        return sampleVO;
    }

    @Override
    public SampleDO to(SampleVO sampleVO) {
        SampleDO sample = sampleVOMapper.to(sampleVO);
        // 自定义规则
        return sample;
    }

}
