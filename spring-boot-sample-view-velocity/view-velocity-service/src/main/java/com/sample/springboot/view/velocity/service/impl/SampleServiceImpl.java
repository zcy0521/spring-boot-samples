package com.sample.springboot.view.velocity.service.impl;

import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.example.SampleExample;
import com.sample.springboot.view.velocity.mapper.SampleMapper;
import com.sample.springboot.view.velocity.mapstruct.query.SampleQueryMapper;
import com.sample.springboot.view.velocity.query.SampleQuery;
import com.sample.springboot.view.velocity.page.Page;
import com.sample.springboot.view.velocity.service.SampleService;
import com.sample.springboot.view.velocity.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SampleServiceImpl extends BaseService implements SampleService {

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private SampleQueryMapper queryMapper;

    @Override
    public List<SampleDO> findAll() {
        return sampleMapper.selectAll();
    }

    @Override
    public List<SampleDO> findAll(int number, int size) {
        startPage(number, size);
        return sampleMapper.selectAll();
    }

    @Override
    public List<SampleDO> findAll(SampleQuery query) {
        SampleExample example = queryMapper.toSampleExample(query);
        return sampleMapper.selectAllByExample(example);
    }

    @Override
    public List<SampleDO> findAll(int number, int size, SampleQuery query) {
        // 查询条件
        SampleExample example = queryMapper.toSampleExample(query);

        // 查询分页对象 优化number
        int totalElements = sampleMapper.countByExample(example);
        Page page = new Page(number, size, totalElements);

        // 分页查询
        startPage(page.getNumber(), page.getSize());
        List<SampleDO> samples = sampleMapper.selectAllByExample(example);
        // 保存分页对象
        query.setPage(page);
        return samples;
    }

    @Override
    public SampleDO findById(Long id) {
        if (null == id) {
            throw new IllegalArgumentException("ID must not be null!");
        }

        return sampleMapper.selectById(id);
    }

    @Override
    public SampleDO findOne(SampleQuery query) {
        SampleExample example = queryMapper.toSampleExample(query);
        return sampleMapper.selectOneByExample(example);
    }

    @Override
    public Long insert(SampleDO entity) {
        if (null == entity) {
            throw new IllegalArgumentException("Entity must not be null!");
        }

        entity.setDisabled(false);
        entity.setGmtCreate(new Date());
        int rows = sampleMapper.insertSelective(entity);
        if (rows > 0) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean update(SampleDO entity) {
        if (null == entity) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        if (null == entity.getId()) {
            throw new IllegalArgumentException("ID must not be null!");
        }

        entity.setGmtModified(new Date());
        int rows = sampleMapper.updateSelective(entity);
        return rows > 0;
    }

    @Override
    public boolean disableById(Long id) {
        if (null == id) {
            log.error("ID must not be null!");
            throw new IllegalArgumentException("ID must not be null!");
        }

        int rows = sampleMapper.disabledById(id);
        return rows > 0;
    }

    @Override
    public int disableByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.error("IDs must not be null!");
            throw new IllegalArgumentException("IDs must not be null!");
        }

        return sampleMapper.disabledByIds(ids);
    }

    @Override
    public int deleteAll() {
        return sampleMapper.deleteAll();
    }

    @Override
    public boolean deleteById(Long id) {
        if (null == id) {
            log.error("ID must not be null!");
            throw new IllegalArgumentException("ID must not be null!");
        }

        int rows = sampleMapper.deleteById(id);
        return rows > 0;
    }

    @Override
    public int deleteByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.error("IDs must not be null!");
            throw new IllegalArgumentException("IDs must not be null!");
        }

        return sampleMapper.deleteByIds(ids);
    }

}
