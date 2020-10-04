package com.sample.springboot.data.mybatis.service;

import com.sample.springboot.data.mybatis.domain.first.FirstDO;
import com.sample.springboot.data.mybatis.query.FirstQuery;
import com.sample.springboot.data.mybatis.service.base.BaseService;

import java.util.List;

public interface FirstService extends BaseService<FirstDO> {

    List<FirstDO> findAll(FirstQuery query);

    List<FirstDO> findAll(FirstQuery query, int number, int size);

}
