package com.sample.springboot.data.redis.service;

import com.sample.springboot.data.redis.domain.standalone.StandaloneDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StandaloneServiceTest {

    @Autowired
    private StandaloneService standaloneService;

    @Test
    public void testSave() {
        StandaloneDO standalone = new StandaloneDO();
        standalone.setId(1L);
        standalone.setGmtCreate(new Date());
        standalone.setDeleted(false);

        standalone.setSampleInteger(222);
        standalone.setSampleFloat(33.3f);
        standalone.setSampleDouble(55.5d);
        standalone.setSampleString("Standalone");
        standalone.setSampleText("This is the Standalone Service test");
        standalone.setSampleDate(LocalDate.of(2012, 3, 26));
        standalone.setSampleTime(LocalTime.of(13, 26, 32));
        standalone.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        standalone.setSampleEnum(6);
        standalone.setSampleAmount("22.2");
        standaloneService.save(standalone);
        assertTrue(standaloneService.existsById(1L));
    }

    @Test
    public void testFindById() {
        StandaloneDO standalone = standaloneService.findById(1L);
        assertNotNull(standalone.getSampleString());
    }

    @Test
    public void testDeleteById() {
        standaloneService.deleteById(1L);
        assertFalse(standaloneService.existsById(1L));
    }

}
