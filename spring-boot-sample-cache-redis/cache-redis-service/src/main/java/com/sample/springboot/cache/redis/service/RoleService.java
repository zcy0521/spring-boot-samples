package com.sample.springboot.cache.redis.service;

import com.sample.springboot.cache.redis.domain.RoleDO;

import java.util.List;

public interface RoleService {

    List<RoleDO> findAll();

    List<RoleDO> findAll(int pageNumber, int pageSize);

    RoleDO findById(Long id);

}
