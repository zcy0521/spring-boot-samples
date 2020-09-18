package com.sample.springboot.mapstruct.mapper.decorator;

import com.sample.springboot.mapstruct.domain.SampleDO;
import com.sample.springboot.mapstruct.mapper.SampleMapper;
import com.sample.springboot.mapstruct.vo.SampleVO;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SampleMapperDecorator implements SampleMapper {

    @Autowired
    private SampleMapper sampleMapper;

    @Override
    public SampleVO toVO(SampleDO sample) {
        SampleVO sampleVO = sampleMapper.toVO(sample);
        // 自定义规则
        return sampleVO;
    }

    @Override
    public SampleDO fromVO(SampleVO sampleVO) {
        SampleDO sample = sampleMapper.fromVO(sampleVO);
        // 自定义规则
        return sample;
    }

}
