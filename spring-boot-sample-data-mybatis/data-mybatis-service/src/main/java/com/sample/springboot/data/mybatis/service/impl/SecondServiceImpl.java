package com.sample.springboot.data.mybatis.service.impl;

import com.sample.springboot.data.mybatis.domain.second.SecondDO;
import com.sample.springboot.data.mybatis.mapper.second.SecondMapper;
import com.sample.springboot.data.mybatis.service.SecondService;
import com.sample.springboot.data.mybatis.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecondServiceImpl extends BaseServiceImpl<SecondDO> implements SecondService {

    private SecondMapper secondMapper;

    /**
     * 注入secondaryMapper(setter注入)
     * JSR-330写法 @Inject @Named("beanName")
     * Spring写法 @Autowired @Qualifier("beanName")
     */
    @Autowired
    public void setSecondaryMapper(SecondMapper secondMapper) {
        // 将"secondaryMapper"注入BaseServiceImpl中
        super.setMapper(secondMapper);
        // 将"secondaryMapper"注入SecondaryServiceImpl中
        this.secondMapper = secondMapper;
    }

}
