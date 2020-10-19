package com.sample.springboot.mapstruct.decorator;

import com.sample.springboot.mapstruct.domain.SampleDO;
import com.sample.springboot.mapstruct.mapper.SampleVOMapper;
import com.sample.springboot.mapstruct.model.SampleVO;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SampleVOMapperDecorator implements SampleVOMapper {

    @Autowired
    private SampleVOMapper sampleVOMapper;

    @Override
    public SampleDO convertTo(SampleVO sampleVO) {
        SampleDO sample = sampleVOMapper.convertTo(sampleVO);
        // 自定义规则
        return sample;
    }

    @Override
    public SampleVO convertFrom(SampleDO sample) {
        SampleVO vo = sampleVOMapper.convertFrom(sample);
        // 自定义规则
        return vo;
    }

}
