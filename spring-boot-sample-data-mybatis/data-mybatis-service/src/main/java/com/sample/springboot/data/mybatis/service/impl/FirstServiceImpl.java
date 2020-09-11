package com.sample.springboot.data.mybatis.service.impl;

import com.sample.springboot.data.mybatis.domain.first.FirstDO;
import com.sample.springboot.data.mybatis.mapper.first.FirstMapper;
import com.sample.springboot.data.mybatis.service.FirstService;
import com.sample.springboot.data.mybatis.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FirstServiceImpl extends BaseServiceImpl<FirstDO> implements FirstService {

    private FirstMapper firstMapper;

    /**
     * 注入primaryMapper(setter注入)
     * JSR-330写法 @Inject @Named("beanName")
     * Spring写法 @Autowired @Qualifier("beanName")
     */
    @Autowired
    public void setMapper(FirstMapper firstMapper) {
        // 将"primaryMapper"注入BaseServiceImpl中
        super.setMapper(firstMapper);
        // 将"primaryMapper"注入PrimaryServiceImpl中
        this.firstMapper = firstMapper;
    }

}
