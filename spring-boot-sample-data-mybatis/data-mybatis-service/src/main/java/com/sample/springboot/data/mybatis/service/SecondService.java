package com.sample.springboot.data.mybatis.service;

import com.sample.springboot.data.mybatis.domain.second.SecondDO;
import com.sample.springboot.data.mybatis.query.SecondQuery;
import com.sample.springboot.data.mybatis.service.base.BaseService;

import java.util.List;

public interface SecondService extends BaseService<SecondDO> {

    List<SecondDO> findAll(SecondQuery query);

    List<SecondDO> findAll(SecondQuery query, int number, int size);

}
