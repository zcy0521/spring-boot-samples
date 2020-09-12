package com.sample.springboot.cache.redis.service.impl;

import com.sample.springboot.cache.redis.common.CachingNames;
import com.sample.springboot.cache.redis.domain.RoleDO;
import com.sample.springboot.cache.redis.mapper.RoleMapper;
import com.sample.springboot.cache.redis.service.RoleService;
import com.sample.springboot.cache.redis.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = CachingNames.ROLE_CACHE)
public class RoleServiceImpl extends BaseService implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    @Cacheable
    public List<RoleDO> findAll() {
        return roleMapper.selectAll();
    }

    @Override
    @Cacheable(key = "#pageNumber.concat('-').concat(#pageSize)")
    public List<RoleDO> findAll(int pageNumber, int pageSize) {
        startPage(pageNumber, pageSize);
        return roleMapper.selectAll();
    }

    @Override
    @Cacheable(key = "#id")
    public RoleDO findById(Long id) {
        if (null == id) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return roleMapper.selectByPrimaryKey(id);
    }

}
