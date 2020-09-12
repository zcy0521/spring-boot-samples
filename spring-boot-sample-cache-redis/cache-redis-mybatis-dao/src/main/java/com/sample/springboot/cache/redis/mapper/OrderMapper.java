package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.OrderDO;
import com.sample.springboot.cache.redis.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface OrderMapper extends BaseMapper<OrderDO> {

    List<OrderDO> selectByUserId(@Param("userId")Long userId);

    List<OrderDO> selectByUserIds(@Param("userIds")Set<Long> userIds);

    int deleteAll();

}