package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.UserDO;
import com.sample.springboot.cache.redis.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserMapper extends BaseMapper<UserDO> {

    List<UserDO> selectByIds(@Param("ids") Set<Long> ids);

    int deleteAll();

}