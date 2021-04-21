package com.sample.springboot.view.velocity.service;

import com.sample.springboot.view.velocity.domain.FileDO;

import java.util.List;
import java.util.Set;

public interface FileService {

    List<FileDO> findAllByIds(Set<Long> ids);

    FileDO findById(Long id);

    Long insert(FileDO entity);

    boolean deleteById(Long id);

    int deleteByIds(Set<Long> ids);

    int deleteAll();

}
