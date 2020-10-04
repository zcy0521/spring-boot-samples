package com.sample.springboot.view.velocity.service;

import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.query.SampleQuery;

import java.util.List;
import java.util.Set;

public interface SampleService {

    List<SampleDO> findAll();

    List<SampleDO> findAll(int number, int size);

    List<SampleDO> findAll(SampleQuery query);

    List<SampleDO> findAll(int number, int size, SampleQuery query);

    SampleDO findById(Long id);

    SampleDO findOne(SampleQuery query);

    Long insert(SampleDO entity);

    boolean update(SampleDO entity);

    boolean disableById(Long id);

    int disableByIds(Set<Long> ids);

}
