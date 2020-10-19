package com.sample.springboot.view.velocity.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.example.SampleExample;
import com.sample.springboot.view.velocity.mapper.SampleMapper;
import com.sample.springboot.view.velocity.page.Page;
import com.sample.springboot.view.velocity.query.SampleQuery;
import com.sample.springboot.view.velocity.service.SampleService;
import com.sample.springboot.view.velocity.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SampleServiceImpl extends BaseService implements SampleService {

    @Autowired
    private SampleMapper sampleMapper;

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
    public List<SampleDO> findAll(SampleQuery query, int number, int size) {
        if (query == null) {
            query = new SampleQuery();
        }

        // 查询条件
        SampleExample example = SampleExample.builder()
                .sampleInteger(query.getSampleInteger())
                .sampleString(query.getSampleString())
                .minAmount(query.getMinAmount())
                .maxAmount(query.getMaxAmount())
                .minDate(query.getMinDate())
                .maxDate(query.getMaxDate())
                .minDateTime(query.getMinDateTime())
                .maxDateTime(query.getMaxDateTime())
                .sampleEnums(query.getSampleEnums())
                .deleted(query.getDeleted())
                .build();

        // 查询分页对象 优化number
        int totalElements = sampleMapper.countByExample(example);
        Page page = new Page(number, size, totalElements);
        query.setPage(page);

        // 分页查询
        startPage(page.getNumber(), page.getSize());
        return sampleMapper.selectAllByExample(example);
    }

    @Override
    public List<SampleDO> findAllByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        List<SampleDO> sampleList = Lists.newArrayList();

        // IN 查询分区
        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            List<SampleDO> _deptList = sampleMapper.selectAllByIds(_ids);
            sampleList.addAll(_deptList);
        });

        return sampleList;
    }

    @Override
    public SampleDO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        return sampleMapper.selectById(id);
    }

    @Override
    public Long insert(SampleDO entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        int count = sampleMapper.insert(entity);
        return count > 0 ? entity.getId() : 0L;
    }

    @Override
    public boolean update(SampleDO entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException();
        }

        SampleDO sample = findById(entity.getId());
        if (sample == null) {
            throw new IllegalArgumentException();
        }

        int count = sampleMapper.updateSelective(entity);
        return count > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        SampleDO sample = findById(id);
        if (sample == null) {
            throw new IllegalArgumentException();
        }

        int count = sampleMapper.deleteById(id);
        return count > 0;
    }

    @Override
    public int deleteByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        List<SampleDO> sampleList = findAllByIds(ids);
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        Set<Long> sampleIds = sampleList.stream()
                .map(SampleDO::getId)
                .collect(Collectors.toSet());

        // IN 删除分区
        Iterables.partition(sampleIds, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _sampleIds = Sets.newHashSet(idList);
            sampleMapper.deleteByIds(_sampleIds);
        });

        return sampleIds.size();
    }

    @Override
    public int deleteAll() {
        return sampleMapper.deleteAll();
    }

}
