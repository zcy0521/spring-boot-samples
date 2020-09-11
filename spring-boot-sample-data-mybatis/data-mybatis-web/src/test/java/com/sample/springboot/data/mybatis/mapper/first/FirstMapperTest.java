package com.sample.springboot.data.mybatis.mapper.first;

import com.sample.springboot.data.mybatis.config.DataSourceConfig;
import com.sample.springboot.data.mybatis.config.FirstConfig;
import com.sample.springboot.data.mybatis.domain.first.FirstDO;
import com.sample.springboot.data.mybatis.enums.SampleEnum;
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

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({DataSourceConfig.class, FirstConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FirstMapperTest {

    @Autowired
    private FirstMapper firstMapper;

    @Test
    @Rollback(false)
    public void testInsert() {
        FirstDO first = new FirstDO();
        first.setSampleInteger(222);
        first.setSampleFloat(33.3f);
        first.setSampleDouble(55.5d);
        first.setSampleString("First");
        first.setSampleText("This is the First Mapper test");
        first.setSampleDate(LocalDate.of(2012, 3, 26));
        first.setSampleTime(LocalTime.of(13, 26, 32));
        first.setSampleDatetime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        first.setSampleEnum(SampleEnum.ENUM_B);
        first.setSampleAmount(new BigDecimal(22.2d));
        first.setSampleValid(true);
        first.setGmtCreate(new Date());
        firstMapper.insertSelective(first);
    }

    @Test
    public void testSelectByPrimaryKey() {
        FirstDO first = firstMapper.selectByPrimaryKey(8L);
        assertNotNull(first);
    }

}
