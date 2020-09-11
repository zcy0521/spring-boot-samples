package com.sample.springboot.data.redis.repository.standalone;

import com.sample.springboot.data.redis.config.ConnectionConfig;
import com.sample.springboot.data.redis.config.StandaloneConfig;
import com.sample.springboot.data.redis.domain.standalone.StandaloneDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataRedisTest
@Import({ConnectionConfig.class, StandaloneConfig.class})
public class StandaloneRepositoryTest {

    @Autowired
    private StandaloneRepository standaloneRepository;

    @Test
    public void testSave() {
        StandaloneDO standalone = new StandaloneDO();
        standalone.setId(1L);
        standalone.setSampleInteger(222);
        standalone.setSampleFloat(33.3f);
        standalone.setSampleDouble(55.5d);
        standalone.setSampleString("Standalone");
        standalone.setSampleText("This is the Standalone Repository test");
        standalone.setSampleDate(LocalDate.of(2012, 3, 26));
        standalone.setSampleTime(LocalTime.of(13, 26, 32));
        standalone.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        standalone.setSampleEnum(6);
        standalone.setSampleAmount("22.2");
        standalone.setSampleValid(true);
        standalone.setGmtCreate(new Date());
        standaloneRepository.save(standalone);
        assertTrue(standaloneRepository.existsById(1L));
    }

    @Test
    public void testFindById() {
        StandaloneDO standalone = standaloneRepository.findById(1L).orElse(new StandaloneDO());
        assertNotNull(standalone.getSampleString());
    }

    @Test
    public void testDeleteById() {
        standaloneRepository.deleteById(1L);
        assertFalse(standaloneRepository.existsById(1L));
    }

    @Test
    public void testDeleteAll() {
        standaloneRepository.deleteAll();
        assertEquals(0, standaloneRepository.count());
    }

}
