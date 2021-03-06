package com.sample.springboot.data.mybatis.service;

import com.sample.springboot.data.mybatis.domain.first.FirstDO;
import com.sample.springboot.data.mybatis.enums.SampleEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FirstServiceTest {

    @Autowired
    private FirstService firstService;

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
        first.setSampleText("This is the First Service test");
        firstService.insert(first);
    }

    @Test
    public void testFindById() {
        FirstDO first = firstService.findById(1L);
        assertNotNull(first);
    }

}
