package com.sample.springboot.view.velocity.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sample.springboot.view.velocity.domain.FileDO;
import com.sample.springboot.view.velocity.mapper.FileMapper;
import com.sample.springboot.view.velocity.mapper.SampleFileMapper;
import com.sample.springboot.view.velocity.service.FileService;
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
public class FileServiceImpl extends BaseService implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private SampleFileMapper sampleFileMapper;

    @Autowired
    private SampleService sampleService;

    @Override
    public List<FileDO> findAllByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        // IN 查询分区
        List<FileDO> fileList = Lists.newArrayList();
        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            List<FileDO> _fileList = fileMapper.selectAllByIds(_ids);
            fileList.addAll(_fileList);
        });
        return fileList;
    }

    @Override
    public FileDO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        return fileMapper.selectById(id);
    }

    @Override
    public Long insert(FileDO entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        int count = fileMapper.insert(entity);
        return count > 0 ? entity.getId() : 0L;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        // 查询待删除对象是否存在
        FileDO target = findById(id);
        if (target == null) {
            return false;
        }

        // 待删除ID
        Long targetId = target.getId();
        if (targetId == null) {
            return false;
        }

        // 删除无效Sample附件
        sampleService.removeFileByFileId(targetId);

        // 删除文件
        int count = fileMapper.deleteById(targetId);
        return count > 0;
    }

    @Override
    public int deleteByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 查询待删除集合是否存在
        List<FileDO> targetList = findAllByIds(ids);
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 待删除ID集合
        Set<Long> targetIds = targetList.stream().map(FileDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(targetIds)) {
            return 0;
        }

        // 删除无效Sample附件
        sampleService.removeFileByFileIds(targetIds);

        // 分区 IN 删除
        Iterables.partition(targetIds, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _sampleIds = Sets.newHashSet(idList);
            fileMapper.deleteByIds(_sampleIds);
        });
        return targetIds.size();
    }

    @Override
    public int deleteAll() {
        sampleFileMapper.deleteAll();
        return fileMapper.deleteAll();
    }
}
