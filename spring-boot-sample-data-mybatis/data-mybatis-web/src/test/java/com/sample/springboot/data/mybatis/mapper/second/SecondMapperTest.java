package com.sample.springboot.data.mybatis.mapper.second;

import com.sample.springboot.data.mybatis.config.DataSourceConfig;
import com.sample.springboot.data.mybatis.config.SecondConfig;
import com.sample.springboot.data.mybatis.domain.second.SecondDO;
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
import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({DataSourceConfig.class, SecondConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SecondMapperTest {

    @Autowired
    private SecondMapper secondMapper;

    @Test
    @Rollback(false)
    public void testInsert() {
        SecondDO second = new SecondDO();
        second.setGmtCreate(new Date());
        second.setDeleted(false);

        second.setSampleInteger(222);
        second.setSampleFloat(33.3f);
        second.setSampleDouble(55.5d);
        second.setSampleString("Second");
        second.setSampleAmount(new BigDecimal("22.20"));
        second.setSampleDate(LocalDate.of(2012, 3, 26));
        second.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        second.setSampleEnum(SampleEnum.ENUM_B);
        second.setSampleText("This is the Second Mapper test");
        secondMapper.insertSelective(second);
    }

    @Test
    public void testSelectByPrimaryKey() {
        SecondDO second = secondMapper.selectByPrimaryKey(1L);
        assertNotNull(second);
    }

}
