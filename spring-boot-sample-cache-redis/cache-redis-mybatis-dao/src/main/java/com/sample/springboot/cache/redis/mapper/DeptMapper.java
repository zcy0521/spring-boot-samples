package com.sample.springboot.cache.redis.mapper;

import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface DeptMapper extends BaseMapper<DeptDO> {

    List<DeptDO> selectByIds(@Param("ids") Set<Long> ids);

    int deleteAll();

}