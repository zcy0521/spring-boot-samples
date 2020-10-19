package com.sample.springboot.data.redis.service;

import com.sample.springboot.data.redis.domain.sentinel.SentinelDO;
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
public class SentinelServiceTest {

    @Autowired
    private SentinelService sentinelService;

    @Test
    public void testSave() {
        SentinelDO sentinel = new SentinelDO();
        sentinel.setId(1L);
        sentinel.setGmtCreate(new Date());
        sentinel.setDeleted(false);

        sentinel.setSampleInteger(222);
        sentinel.setSampleFloat(33.3f);
        sentinel.setSampleDouble(55.5d);
        sentinel.setSampleString("Sentinel");
        sentinel.setSampleText("This is the Sentinel Service test");
        sentinel.setSampleDate(LocalDate.of(2012, 3, 26));
        sentinel.setSampleTime(LocalTime.of(13, 26, 32));
        sentinel.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        sentinel.setSampleEnum(6);
        sentinel.setSampleAmount("22.2");
        sentinelService.save(sentinel);
        assertTrue(sentinelService.existsById(1L));
    }

    @Test
    public void testFindById() {
        SentinelDO sentinel = sentinelService.findById(1L);
        assertNotNull(sentinel.getSampleString());
    }

    @Test
    public void testDeleteById() {
        sentinelService.deleteById(1L);
        assertFalse(sentinelService.existsById(1L));
    }

}
