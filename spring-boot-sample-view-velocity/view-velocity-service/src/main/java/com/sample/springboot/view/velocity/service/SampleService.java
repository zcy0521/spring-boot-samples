package com.sample.springboot.view.velocity.service;

import com.sample.springboot.view.velocity.domain.FileDO;
import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.domain.SampleFileDO;
import com.sample.springboot.view.velocity.query.SampleQuery;

import java.util.List;
import java.util.Set;

public interface SampleService {

    List<SampleDO> findAll();

    List<SampleDO> findAll(SampleQuery query);

    List<SampleDO> findAllByIds(Set<Long> ids);

    SampleDO findById(Long id);

    Long insert(SampleDO entity);

    boolean update(SampleDO entity);

    boolean deleteById(Long id);

    int deleteByIds(Set<Long> ids);

    int deleteAll();


    void saveFile(Long sampleId, FileDO file);

    void saveFiles(Long sampleId, List<FileDO> files);

    List<FileDO> findAllFiles(Long sampleId);

    boolean removeFile(SampleFileDO sampleFile);

    int removeFiles(List<SampleFileDO> sampleFiles);

    int removeFileBySampleId(Long sampleId);

    int removeFileBySampleIds(Set<Long> sampleId);

    int removeFileByFileId(Long fileId);

    int removeFileByFileIds(Set<Long> fileIds);

}
