package com.sample.springboot.rest.server.service;

import com.sample.springboot.rest.server.domain.SampleDO;
import com.sample.springboot.rest.server.query.SampleQuery;

import java.util.List;
import java.util.Set;

public interface SampleService {

    List<SampleDO> findAll();

    List<SampleDO> findAll(int number, int size);

    List<SampleDO> findAll(SampleQuery query, int number, int size);

    List<SampleDO> findAllByIds(Set<Long> ids);

    SampleDO findById(Long id);

    Long insert(SampleDO entity);

    boolean update(SampleDO entity);

    boolean deleteById(Long id);

    int deleteByIds(Set<Long> ids);

    int deleteAll();

}
