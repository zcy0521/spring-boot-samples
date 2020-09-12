package com.sample.springboot.cache.redis.service.impl;

import com.sample.springboot.cache.redis.common.CachingNames;
import com.sample.springboot.cache.redis.domain.DeptDO;
import com.sample.springboot.cache.redis.mapper.DeptMapper;
import com.sample.springboot.cache.redis.service.DeptService;
import com.sample.springboot.cache.redis.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = CachingNames.DEPT_CACHE)
public class DeptServiceImpl extends BaseService implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    @Cacheable
    public List<DeptDO> findAll() {
        return deptMapper.selectAll();
    }

    @Override
    @Cacheable(key = "#pageNumber.concat('-').concat(#pageSize)")
    public List<DeptDO> findAll(int pageNumber, int pageSize) {
        startPage(pageNumber, pageSize);
        return deptMapper.selectAll();
    }

    @Override
    @Cacheable(key = "#id")
    public DeptDO findById(Long id) {
        if (null == id) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return deptMapper.selectByPrimaryKey(id);
    }

}
