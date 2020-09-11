package com.sample.springboot.data.jpa.service.impl;

import com.sample.springboot.data.jpa.domain.second.SecondDO;
import com.sample.springboot.data.jpa.repository.second.SecondRepository;
import com.sample.springboot.data.jpa.service.SecondService;
import com.sample.springboot.data.jpa.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecondServiceImpl extends BaseServiceImpl<SecondDO, Long> implements SecondService {

    private SecondRepository secondRepository;

    /**
     * 注入sampleRepository(setter注入)
     * JSR-330写法 @Inject @Named("beanName")
     * Spring写法 @Autowired @Qualifier("beanName")
     */
    @Autowired
    public void setRepository(SecondRepository secondRepository) {
        // 将"secondRepository"注入BaseServiceImpl中
        super.setRepository(secondRepository);
        // 将"secondRepository"注入SecondaryServiceImpl中
        this.secondRepository = secondRepository;
    }

    @Override
    public SecondDO searchById(Long id) {
        return secondRepository.searchById(id);
    }

}
