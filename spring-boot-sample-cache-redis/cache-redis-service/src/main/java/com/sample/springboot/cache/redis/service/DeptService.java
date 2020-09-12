package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.DeptDO;

import java.util.List;

public interface DeptService {

    List<DeptDO> findAll();

    List<DeptDO> findAll(int pageNumber, int pageSize);

    DeptDO findById(Long id);

}
