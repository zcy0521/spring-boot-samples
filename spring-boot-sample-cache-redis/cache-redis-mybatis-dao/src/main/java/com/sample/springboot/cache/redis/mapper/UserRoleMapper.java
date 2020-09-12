package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.UserRoleDO;
import com.sample.springboot.cache.redis.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

    List<UserRoleDO> selectByUserId(@Param("userId")Long userId);

    List<UserRoleDO> selectByUserIds(@Param("userIds")Set<Long> userIds);

    int deleteAll();

}