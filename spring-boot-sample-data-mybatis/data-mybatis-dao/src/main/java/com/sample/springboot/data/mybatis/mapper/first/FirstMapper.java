package com.sample.springboot.data.mybatis.mapper.first;

import com.sample.springboot.data.mybatis.domain.first.FirstDO;
import com.sample.springboot.data.mybatis.example.first.FirstExample;
import com.sample.springboot.data.mybatis.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface FirstMapper extends BaseMapper<FirstDO> {

    List<FirstDO> selectAllByIds(@Param("ids") Set<Long> ids);

    List<FirstDO> selectAllByExample(FirstExample example);

}