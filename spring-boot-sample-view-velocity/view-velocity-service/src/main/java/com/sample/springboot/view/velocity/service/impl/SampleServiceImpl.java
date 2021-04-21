package com.sample.springboot.view.velocity.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sample.springboot.view.velocity.domain.FileDO;
import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.domain.SampleFileDO;
import com.sample.springboot.view.velocity.example.SampleExample;
import com.sample.springboot.view.velocity.example.SampleFileExample;
import com.sample.springboot.view.velocity.mapper.SampleFileMapper;
import com.sample.springboot.view.velocity.mapper.SampleMapper;
import com.sample.springboot.view.velocity.page.Page;
import com.sample.springboot.view.velocity.query.SampleQuery;
import com.sample.springboot.view.velocity.service.FileService;
import com.sample.springboot.view.velocity.service.SampleService;
import com.sample.springboot.view.velocity.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SampleServiceImpl extends BaseService implements SampleService {

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private SampleFileMapper sampleFileMapper;

    @Autowired
    private FileService fileService;

    @Override
    public List<SampleDO> findAll() {
        return sampleMapper.selectAll();
    }

    @Override
    public List<SampleDO> findAll(SampleQuery query) {
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
        Page page = new Page(query.getNumber(), query.getSize(), totalElements);
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

        // IN 查询分区
        List<SampleDO> sampleList = Lists.newArrayList();
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
            return false;
        }

        // 查询待删除对象是否存在
        SampleDO target = findById(id);
        if (target == null) {
            return false;
        }

        // 待删除ID
        Long targetId = target.getId();
        if (targetId == null) {
            return false;
        }

        // 删除无效附件
        removeFileBySampleId(targetId);

        // 删除Sample
        int count = sampleMapper.deleteById(targetId);
        return count > 0;
    }

    @Override
    public int deleteByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 查询待删除集合是否存在
        List<SampleDO> targetList = findAllByIds(ids);
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 待删除ID集合
        Set<Long> targetIds = targetList.stream().map(SampleDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(targetIds)) {
            return 0;
        }

        // 删除无效附件
        removeFileBySampleIds(targetIds);

        // 分区 IN 删除
        Iterables.partition(targetIds, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _sampleIds = Sets.newHashSet(idList);
            sampleMapper.deleteByIds(_sampleIds);
        });
        return targetIds.size();
    }

    @Override
    public int deleteAll() {
        return sampleMapper.deleteAll();
    }

    @Override
    public void saveFile(Long sampleId, FileDO file) {
        Objects.requireNonNull(sampleId);
        Objects.requireNonNull(file);

        // File存入DB
        Long fileId = fileService.insert(file);

        // 查询附件
        SampleFileExample example = SampleFileExample
                .builder()
                .sampleId(sampleId)
                .fileId(fileId)
                .build();
        SampleFileDO sampleFile = sampleFileMapper.selectOneByExample(example);

        // 附件已存在
        if (sampleFile != null) {
            return;
        }

        // 保存附件
        sampleFile = new SampleFileDO();
        sampleFile.setSampleId(sampleId);
        sampleFile.setFileId(fileId);
        sampleFile.setFileType(file.getFileType());
        sampleFileMapper.insert(sampleFile);
    }

    @Override
    public void saveFiles(Long sampleId, List<FileDO> files) {
        files.forEach(file -> saveFile(sampleId, file));
    }

    @Override
    public List<FileDO> findAllFiles(Long sampleId) {
        // 查询附件
        SampleFileExample example = SampleFileExample
                .builder()
                .sampleId(sampleId)
                .build();
        List<SampleFileDO> sampleFileList = sampleFileMapper.selectAllByExample(example);

        // 无附件
        if (CollectionUtils.isEmpty(sampleFileList)) {
            return Lists.newArrayList();
        }

        // 查询文件列表
        Set<Long> fileIds = sampleFileList.stream()
                .map(SampleFileDO::getFileId)
                .collect(Collectors.toSet());
        List<FileDO> fileList = fileService.findAllByIds(fileIds);

        // 文件不存在
        if (CollectionUtils.isEmpty(fileList)) {
            return Lists.newArrayList();
        }

        // 文件ID Map
        Map<Long, FileDO> fileIdMap = fileList.stream().collect(Collectors.toMap(
                FileDO::getId,
                Function.identity(),
                (first, second) -> first
        ));

        // 返回Files集合 包含sampleId fileType 用来区分不同Sample、类型的附件
        List<FileDO> files = Lists.newArrayList();
        sampleFileList.forEach(sampleFile -> {
            FileDO _file = fileIdMap.get(sampleFile.getFileId());
            FileDO file = new FileDO();
            file.setId(_file.getId());
            file.setFileName(_file.getFileName());
            file.setSize(_file.getSize());
            file.setContentType(_file.getContentType());
            file.setSampleId(sampleFile.getSampleId());
            file.setFileType(sampleFile.getFileType());
            files.add(file);
        });
        return files;
    }

    @Override
    public boolean removeFile(SampleFileDO sampleFile) {
        if (sampleFile == null) {
            return false;
        }
        if (sampleFile.getId() == null) {
            return false;
        }

        // 查询待删除对象是否存在
        SampleFileDO target = sampleFileMapper.selectById(sampleFile.getId());
        if (target == null) {
            return false;
        }

        int count = sampleFileMapper.deleteById(target.getId());
        return count > 0;
    }

    @Override
    public int removeFiles(List<SampleFileDO> sampleFiles) {
        if (CollectionUtils.isEmpty(sampleFiles)) {
            return 0;
        }

        Set<Long> ids = sampleFiles.stream().map(SampleFileDO::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // 查询待删除集合是否存在
        List<SampleFileDO> targetList = Lists.newArrayList();
        Iterables.partition(ids, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _ids = Sets.newHashSet(idList);
            List<SampleFileDO> _targetList = sampleFileMapper.selectAllByIds(_ids);
            targetList.addAll(_targetList);
        });

        // 待删除ID集合
        Set<Long> targetIds = targetList.stream().map(SampleFileDO::getId).collect(Collectors.toSet());

        // 分区 IN 删除
        Iterables.partition(targetIds, PARTITION_SIZE).forEach(idList -> {
            Set<Long> _sampleFileIds = Sets.newHashSet(idList);
            sampleFileMapper.deleteByIds(_sampleFileIds);
        });
        return targetIds.size();
    }

    @Override
    public int removeFileBySampleId(Long sampleId) {
        SampleFileExample example = SampleFileExample
                .builder()
                .sampleId(sampleId)
                .build();
        List<SampleFileDO> sampleFiles = sampleFileMapper.selectAllByExample(example);
        return removeFiles(sampleFiles);
    }

    @Override
    public int removeFileBySampleIds(Set<Long> sampleIds) {
        SampleFileExample example = SampleFileExample
                .builder()
                .sampleIds(sampleIds)
                .build();
        List<SampleFileDO> sampleFiles = sampleFileMapper.selectAllByExample(example);
        return removeFiles(sampleFiles);
    }

    @Override
    public int removeFileByFileId(Long fileId) {
        SampleFileExample example = SampleFileExample
                .builder()
                .fileId(fileId)
                .build();
        List<SampleFileDO> sampleFiles = sampleFileMapper.selectAllByExample(example);
        return removeFiles(sampleFiles);
    }

    @Override
    public int removeFileByFileIds(Set<Long> fileIds) {
        SampleFileExample example = SampleFileExample
                .builder()
                .fileIds(fileIds)
                .build();
        List<SampleFileDO> sampleFiles = sampleFileMapper.selectAllByExample(example);
        return removeFiles(sampleFiles);
    }

}
