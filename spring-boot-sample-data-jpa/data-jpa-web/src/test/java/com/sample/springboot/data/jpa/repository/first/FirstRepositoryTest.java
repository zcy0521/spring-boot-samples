package com.sample.springboot.data.jpa.repository.first;

import com.sample.springboot.data.jpa.config.DataSourceConfig;
import com.sample.springboot.data.jpa.config.FirstConfig;
import com.sample.springboot.data.jpa.domain.first.FirstDO;
import com.sample.springboot.data.jpa.enums.SampleEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({DataSourceConfig.class, FirstConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FirstRepositoryTest {

    @Autowired
    private FirstRepository firstRepository;

    @Test
    @Rollback(false)
    public void testSave() {
        FirstDO first = new FirstDO();
        first.setSampleInteger(222);
        first.setSampleFloat(33.3f);
        first.setSampleDouble(55.5d);
        first.setSampleString("First");
        first.setSampleText("This is the First Repository test");
        first.setSampleDate(LocalDate.of(2012, 3, 26));
        first.setSampleTime(LocalTime.of(13, 26, 32));
        first.setSampleDatetime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        first.setSampleEnum(SampleEnum.ENUM_B);
        first.setSampleAmount(new BigDecimal(22.2d));
        first.setSampleValid(true);
        first.setGmtCreate(new Date());
        first = firstRepository.save(first);
        assertNotNull(first.getId());
    }

    @Test
    public void testFindById() {
        FirstDO first = firstRepository.findById(1L).orElse(new FirstDO());
        assertNotNull(first.getSampleString());
        assertEquals(SampleEnum.ENUM_B, first.getSampleEnum());
    }

    @Test
    public void testSearchById() {
        FirstDO first = firstRepository.searchById(1L);
        assertNotNull(first.getSampleString());
        assertEquals(SampleEnum.ENUM_B, first.getSampleEnum());
    }

}
