package com.sample.springboot.alipay.orika.mapper;

import com.sample.springboot.alipay.domain.OrderDO;
import com.sample.springboot.alipay.model.OrderVO;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderVOMapper {

    @Autowired
    private MapperFactory mapperFactory;

    public OrderDO toOrder(OrderVO orderVO) {
        return mapperFactory.getMapperFacade().map(orderVO, OrderDO.class);
    }

    public List<OrderDO> toOrderList(List<OrderVO> orderVOList) {
        return mapperFactory.getMapperFacade().mapAsList(orderVOList, OrderDO.class);
    }

    public OrderVO fromOrder(OrderDO order) {
        return mapperFactory.getMapperFacade().map(order, OrderVO.class);
    }

    public List<OrderVO> fromOrderList(List<OrderDO> orderList) {
        return mapperFactory.getMapperFacade().mapAsList(orderList, OrderVO.class);
    }

}
