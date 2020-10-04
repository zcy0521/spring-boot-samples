package com.sample.springboot.data.jpa.repository.second;

import com.sample.springboot.data.jpa.config.DataSourceConfig;
import com.sample.springboot.data.jpa.config.SecondConfig;
import com.sample.springboot.data.jpa.domain.second.SecondDO;
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
import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({DataSourceConfig.class, SecondConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SecondRepositoryTest {

    @Autowired
    private SecondRepository secondRepository;

    @Test
    @Rollback(false)
    public void testSave() {
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
        second.setSampleText("This is the Second Repository test");
        second.setDisabled(false);
        second = secondRepository.save(second);
        assertNotNull(second.getId());
    }

    @Test
    public void testFindById() {
        SecondDO second = secondRepository.findById(1L).orElse(new SecondDO());
        assertNotNull(second.getSampleString());
    }

    @Test
    public void testSearchById() {
        SecondDO second = secondRepository.searchById(1L);
        assertNotNull(second.getSampleString());
    }

}
