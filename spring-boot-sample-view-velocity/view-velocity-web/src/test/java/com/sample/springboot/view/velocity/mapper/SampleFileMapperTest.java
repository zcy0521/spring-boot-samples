package com.sample.springboot.view.velocity.mapper;

import com.google.common.collect.Sets;
import com.sample.springboot.view.velocity.config.MyBatisConfig;
import com.sample.springboot.view.velocity.domain.SampleFileDO;
import com.sample.springboot.view.velocity.enums.FileType;
import com.sample.springboot.view.velocity.example.SampleFileExample;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@MybatisTest
@Import({MyBatisConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class SampleFileMapperTest {

    @Autowired
    private SampleFileMapper mapper;

    @Test
    public void testInsert() {
        for (int i = 1; i <= 6; i++) {
            SampleFileDO sampleFile = new SampleFileDO();
            sampleFile.setSampleId(1L);
            sampleFile.setFileId((long) i);
            sampleFile.setFileType(FileType.resolve(i));
            int count = mapper.insert(sampleFile);
            assertThat(count, is(1));
            assertThat(sampleFile.getId(), notNullValue());
        }
    }

    @Test
    public void testUpdateSelective() {
        SampleFileDO sampleFile = new SampleFileDO();
        sampleFile.setId(1L);
        sampleFile.setFileId(2L);
        int count = mapper.updateSelective(sampleFile);
        assertThat(count, is(1));
    }

    @Test
    public void testSelectAll() {
        List<SampleFileDO> sampleFiles = mapper.selectAll();
        int count = mapper.countAll();
        assertThat(sampleFiles, hasSize(count));
    }

    @Test
    public void testSelectAllByExample() {
        SampleFileExample example = SampleFileExample.builder()
                .sampleId(1L)
                .fileTypes(new FileType[]{FileType.IMAGE, FileType.AUDIO})
                .build();
        List<SampleFileDO> sampleFiles = mapper.selectAllByExample(example);
        assertThat(sampleFiles, not(empty()));
    }

    @Test
    public void testDeleteById() {
        int count = mapper.deleteById(1L);
        assertThat(count, is(1));

        count = mapper.deleteById(null);
        assertThat(count, is(0));
    }

    @Test
    public void testDeleteByIds() {
        int count = mapper.deleteByIds(Sets.newHashSet(2L, 3L));
        assertThat(count, is(2));

        count = mapper.deleteByIds(null);
        assertThat(count, is(0));

        count = mapper.deleteByIds(Sets.newHashSet());
        assertThat(count, is(0));
    }

    @Test
    public void testDeleteAll() {
        mapper.deleteAll();
        int count = mapper.countAll();
        assertThat(count, is(0));
    }

}
