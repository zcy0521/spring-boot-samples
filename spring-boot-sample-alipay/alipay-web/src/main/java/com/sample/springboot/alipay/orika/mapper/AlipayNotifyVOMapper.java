package com.sample.springboot.alipay.orika.mapper;

import com.sample.springboot.alipay.domain.AlipayNotifyDO;
import com.sample.springboot.alipay.model.AlipayNotifyVO;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlipayNotifyVOMapper {

    @Autowired
    private MapperFactory mapperFactory;

    public AlipayNotifyDO toAlipayNotify(AlipayNotifyVO notifyVO) {
        return mapperFactory.getMapperFacade().map(notifyVO, AlipayNotifyDO.class);
    }

}
