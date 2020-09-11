package com.sample.springboot.data.jpa.service.impl;

import com.sample.springboot.data.jpa.domain.first.FirstDO;
import com.sample.springboot.data.jpa.repository.first.FirstRepository;
import com.sample.springboot.data.jpa.service.FirstService;
import com.sample.springboot.data.jpa.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirstServiceImpl extends BaseServiceImpl<FirstDO, Long> implements FirstService {

    private FirstRepository firstRepository;

    /**
     * 注入sampleRepository(setter注入)
     * JSR-330写法 @Inject @Named("beanName")
     * Spring写法 @Autowired @Qualifier("beanName")
     */
    @Autowired
    public void setRepository(FirstRepository firstRepository) {
        // 将"firstRepository"注入BaseServiceImpl中
        super.setRepository(firstRepository);
        // 将"firstRepository"注入PrimaryServiceImpl中
        this.firstRepository = firstRepository;
    }

    @Override
    public FirstDO searchById(Long id) {
        return firstRepository.searchById(id);
    }

}
