package com.sample.springboot.view.velocity.mapper;

import com.google.common.collect.Sets;
import com.sample.springboot.view.velocity.config.MyBatisConfig;
import com.sample.springboot.view.velocity.domain.FileDO;
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
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@MybatisTest
@Import({MyBatisConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class FileMapperTest {

    @Autowired
    private FileMapper mapper;

    @Test
    public void testInsert() {
        for (int i = 0; i < 10; i++) {
            FileDO file = new FileDO();
            file.setFileName("/test/test" + (i + 1) + ".txt");
            file.setContentType("txt");
            file.setSize(200L);
            int count = mapper.insert(file);
            assertThat(count, is(1));
            assertThat(file.getId(), notNullValue());
        }
    }

    @Test
    public void testUpdateSelective() {
        FileDO file = new FileDO();
        file.setId(1L);
        file.setFileName("/test1/test.txt");
        int count = mapper.updateSelective(file);
        assertThat(count, is(1));
    }

    @Test
    public void testSelectAll() {
        List<FileDO> files = mapper.selectAll();
        int count = mapper.countAll();
        assertThat(files, hasSize(count));
    }

    @Test
    public void testSelectAllByIds() {
        Set<Long> ids = Sets.newHashSet(1L, 2L);
        List<FileDO> files = mapper.selectAllByIds(ids);
        assertThat(files, hasSize(2));

        files = mapper.selectAllByIds(null);
        assertThat(files, hasSize(0));

        files = mapper.selectAllByIds(Sets.newHashSet());
        assertThat(files, hasSize(0));
    }

    @Test
    public void testSelectById() {
        Long id = 1L;
        FileDO file = mapper.selectById(id);
        assertThat(file, notNullValue());

        file = mapper.selectById(null);
        assertThat(file, nullValue());
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
