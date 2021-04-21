package com.sample.springboot.rest.client.service;

import com.sample.springboot.rest.client.model.Sample;
import com.sample.springboot.rest.client.query.SampleQuery;

import java.util.List;
import java.util.Set;

public interface SampleService {

    List<Sample> findAll(SampleQuery query);

    Sample findById(Long id);

    Long create(Sample sample);

    boolean update(Sample sample);

    boolean deleteById(Long id);

    int deleteByIds(Set<Long> ids);

}
