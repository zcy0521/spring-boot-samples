package com.sample.springboot.data.mybatis.mapper.first;

import com.google.common.collect.Sets;
import com.sample.springboot.data.mybatis.config.DataSourceConfig;
import com.sample.springboot.data.mybatis.config.FirstConfig;
import com.sample.springboot.data.mybatis.domain.first.FirstDO;
import com.sample.springboot.data.mybatis.enums.SampleEnum;
import com.sample.springboot.data.mybatis.example.first.FirstExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({DataSourceConfig.class, FirstConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class FirstMapperTest {

    @Autowired
    private FirstMapper firstMapper;

    @Test
    public void testSelectAllByIds() {
        Set<Long> ids = Sets.newHashSet(1L, 2L);
        List<FirstDO> firsts = firstMapper.selectAllByIds(ids);
        assertThat(firsts, hasSize(2));
    }

    @Test
    public void testSelectAllByExample() {
        FirstExample example = new FirstExample();
        example.setSampleString("1");
        example.setMaxDate(LocalDate.now());
        example.setSampleEnums(new SampleEnum[]{SampleEnum.ENUM_A, SampleEnum.ENUM_B});
        List<FirstDO> firsts = firstMapper.selectAllByExample(example);
        assertThat(firsts, hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testSelectByExample() {
        Set<Long> ids = Sets.newHashSet(1L, 2L);
        Example example = Example.builder(FirstDO.class)
                .where(WeekendSqls.<FirstDO>custom().andIn(FirstDO::getSampleEnum, ids))
                .orderByAsc("id")
                .build();
        List<FirstDO> firsts = firstMapper.selectByExample(example);
        assertThat(firsts, hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testInsert() {
        FirstDO first = new FirstDO();
        first.setGmtCreate(new Date());
        first.setDeleted(false);

        first.setSampleInteger(222);
        first.setSampleFloat(33.3f);
        first.setSampleDouble(55.5d);
        first.setSampleString("First");
        first.setSampleAmount(new BigDecimal("22.20"));
        first.setSampleDate(LocalDate.of(2012, 3, 26));
        first.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        first.setSampleEnum(SampleEnum.ENUM_B);
        first.setSampleText("This is the First Mapper test");
        int count = firstMapper.insertSelective(first);
        assertThat(count, is(1));
    }

    @Test
    public void testSelectByPrimaryKey() {
        FirstDO first = firstMapper.selectByPrimaryKey(8L);
        assertThat(first, nullValue());
    }

}
