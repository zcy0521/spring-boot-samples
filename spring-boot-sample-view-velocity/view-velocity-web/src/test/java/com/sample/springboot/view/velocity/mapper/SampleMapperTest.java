package com.sample.springboot.view.velocity.mapper;

import com.google.common.collect.Sets;
import com.sample.springboot.view.velocity.config.MyBatisConfig;
import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.enums.SampleEnum;
import com.sample.springboot.view.velocity.example.SampleExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@MybatisTest
@Import({MyBatisConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class SampleMapperTest {

    @Autowired
    private SampleMapper mapper;

    @Test
    public void testInsert() {
        SampleDO sample = new SampleDO();
        sample.setSampleInteger(222);
        sample.setSampleDouble(55.5d);
        sample.setSampleString("SampleString");
        sample.setSampleAmount(new BigDecimal("22.20"));
        sample.setSampleDate(LocalDate.of(2012, 3, 26));
        sample.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        sample.setSampleEnum(SampleEnum.ENUM_A);
        sample.setSampleText("SampleText");
        int count = mapper.insert(sample);
        assertThat(count, is(1));
        assertThat(sample.getId(), notNullValue());
    }

    @Test
    public void testInsertSelective() {
        SampleDO sample = new SampleDO();
        sample.setSampleInteger(222);
        sample.setSampleString("SampleString");
        sample.setSampleAmount(new BigDecimal("22.20"));
        sample.setSampleDate(LocalDate.of(2012, 3, 26));
        sample.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        sample.setSampleEnum(SampleEnum.ENUM_A);
        sample.setSampleText("SampleText");
        int count = mapper.insertSelective(sample);
        assertThat(count, is(1));
        assertThat(sample.getId(), notNullValue());
    }

    @Test
    public void testUpdate() {
        SampleDO sample = new SampleDO();
        sample.setId(1L);
        sample.setSampleInteger(null);
        sample.setSampleString("");
        sample.setSampleAmount(new BigDecimal("33.30"));
        sample.setSampleDate(LocalDate.of(2013, 4, 27));
        sample.setSampleDateTime(LocalDateTime.of(2019, 9, 10, 13, 33, 20));
        sample.setSampleEnum(SampleEnum.ENUM_B);
        sample.setSampleText("SampleTextEdit");
        int count = mapper.update(sample);
        assertThat(count, is(1));
    }

    @Test
    public void testUpdateSelective() {
        SampleDO sample = new SampleDO();
        sample.setId(2L);
        sample.setSampleInteger(null);
        sample.setSampleString("");
        sample.setSampleAmount(new BigDecimal("33.30"));
        sample.setSampleDate(LocalDate.of(2013, 4, 27));
        sample.setSampleDateTime(LocalDateTime.of(2019, 9, 10, 13, 33, 20));
        sample.setSampleEnum(SampleEnum.ENUM_C);
        sample.setSampleText("SampleTextEdit");
        int count = mapper.updateSelective(sample);
        assertThat(count, is(1));
    }

    @Test
    public void testSelectAll() {
        List<SampleDO> samples = mapper.selectAll();
        int count = mapper.countAll();
        assertThat(samples, hasSize(count));
    }

    @Test
    public void testSelectAllByExample() {
        SampleExample example = new SampleExample();
        example.setSampleString("1");
        example.setMaxDate(LocalDate.now());
        example.setSampleEnums(new SampleEnum[]{SampleEnum.ENUM_A, SampleEnum.ENUM_B});
        List<SampleDO> samples = mapper.selectAllByExample(example);
        int count = mapper.countByExample(example);
        assertThat(samples, hasSize(count));
    }

    @Test
    public void testSelectAllByIds() {
        Set<Long> ids = Sets.newHashSet(1L, 2L);
        List<SampleDO> samples = mapper.selectAllByIds(ids);
        assertThat(samples, hasSize(2));

        List<SampleDO> nullSamples = mapper.selectAllByIds(null);
        assertThat(nullSamples, hasSize(0));

        List<SampleDO> emptySamples = mapper.selectAllByIds(Sets.newHashSet());
        assertThat(emptySamples, hasSize(0));
    }

    @Test
    public void testSelectById() {
        Long id = 1L;
        SampleDO sample = mapper.selectById(id);
        assertThat(sample, notNullValue());

        SampleDO nullSamples = mapper.selectById(null);
        assertThat(nullSamples, nullValue());
    }

    @Test
    public void testDisabledById() {
        Long id = 1L;
        int count = mapper.disabledById(id);
        assertThat(count, is(1));

        count = mapper.disabledById(null);
        assertThat(count, is(0));
    }

    @Test
    public void testDisabledByIds() {
        Set<Long> ids = Sets.newHashSet(1L, 2L);
        int count = mapper.disabledByIds(ids);
        assertThat(count, is(2));

        count = mapper.disabledByIds(null);
        assertThat(count, is(0));

        count = mapper.disabledByIds(Sets.newHashSet());
        assertThat(count, is(0));
    }

    @Test
    public void testCountAll() {
        int count = mapper.countAll();
        int expected = mapper.selectAll().size();
        assertThat(count, is(expected));
    }

    @Test
    public void testCountByExample() {
        SampleExample example = new SampleExample();
        example.setSampleString("1");
        example.setMaxDate(LocalDate.now());
        example.setSampleEnums(new SampleEnum[]{SampleEnum.ENUM_A, SampleEnum.ENUM_B});
        int count = mapper.countByExample(example);
        int expected = mapper.selectAllByExample(example).size();
        assertThat(count, is(expected));
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;
        int count = mapper.deleteById(id);
        assertThat(count, is(1));

        count = mapper.deleteById(null);
        assertThat(count, is(0));
    }

    @Test
    public void testDeleteByIds() {
        Set<Long> ids = Sets.newHashSet(1L, 2L);
        int count = mapper.deleteByIds(ids);
        assertThat(count, is(2));

        count = mapper.deleteByIds(null);
        assertThat(count, is(0));

        count = mapper.deleteByIds(Sets.newHashSet());
        assertThat(count, is(0));
    }

    @Test
    public void testDeleteAll() {
        int count = mapper.deleteAll();
        int expected = mapper.countAll();
        assertThat(count, is(expected));
    }

}
