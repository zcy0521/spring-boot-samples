package com.sample.springboot.data.redis.repository.sentinel;

import com.sample.springboot.data.redis.config.ConnectionConfig;
import com.sample.springboot.data.redis.config.SentinelConfig;
import com.sample.springboot.data.redis.domain.sentinel.SentinelDO;
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
@Import({ConnectionConfig.class, SentinelConfig.class})
public class SentinelRepositoryTest {

    @Autowired
    private SentinelRepository secondRepository;

    @Test
    public void testSave() {
        SentinelDO sentinel = new SentinelDO();
        sentinel.setId(1L);
        sentinel.setSampleInteger(222);
        sentinel.setSampleFloat(33.3f);
        sentinel.setSampleDouble(55.5d);
        sentinel.setSampleString("Sentinel");
        sentinel.setSampleText("This is the Sentinel Repository test");
        sentinel.setSampleDate(LocalDate.of(2012, 3, 26));
        sentinel.setSampleTime(LocalTime.of(13, 26, 32));
        sentinel.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        sentinel.setSampleEnum(6);
        sentinel.setSampleAmount("22.2");
        sentinel.setSampleValid(true);
        sentinel.setGmtCreate(new Date());
        secondRepository.save(sentinel);
        assertTrue(secondRepository.existsById(1L));
    }

    @Test
    public void testFindById() {
        SentinelDO sentinel = secondRepository.findById(1L).orElse(new SentinelDO());
        assertNotNull(sentinel.getSampleString());
    }

    @Test
    public void testDeleteById() {
        secondRepository.deleteById(1L);
        assertFalse(secondRepository.existsById(1L));
    }

    @Test
    public void testDeleteAll() {
        secondRepository.deleteAll();
        assertEquals(0, secondRepository.count());
    }

}
