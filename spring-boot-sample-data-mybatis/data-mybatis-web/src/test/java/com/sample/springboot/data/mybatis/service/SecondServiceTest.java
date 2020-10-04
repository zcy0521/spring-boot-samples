package com.sample.springboot.data.mybatis.service;

import com.sample.springboot.data.mybatis.domain.second.SecondDO;
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
public class SecondServiceTest {

    @Autowired
    private SecondService secondService;

    @Test
    public void testInsert() {
        SecondDO second = new SecondDO();
        second.setGmtCreate(new Date());
        second.setSampleInteger(222);
        second.setSampleFloat(33.3f);
        second.setSampleDouble(55.5d);
        second.setSampleString("Second");
        second.setSampleAmount(new BigDecimal("22.20"));
        second.setSampleDate(LocalDate.of(2012, 3, 26));
        second.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        second.setSampleEnum(SampleEnum.ENUM_B);
        second.setSampleText("This is the Second Service test");
        second.setDisabled(true);
        secondService.insert(second);
    }

    @Test
    public void testFindById() {
        SecondDO second = secondService.findById(1L);
        assertNotNull(second);
    }

}
