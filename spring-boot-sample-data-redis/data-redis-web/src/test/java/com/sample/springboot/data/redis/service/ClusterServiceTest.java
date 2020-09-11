package com.sample.springboot.data.redis.service;

import com.sample.springboot.data.redis.domain.cluster.ClusterDO;
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
public class ClusterServiceTest {

    @Autowired
    private ClusterService clusterService;

    @Test
    public void testSave() {
        ClusterDO cluster = new ClusterDO();
        cluster.setId(1L);
        cluster.setSampleInteger(222);
        cluster.setSampleFloat(33.3f);
        cluster.setSampleDouble(55.5d);
        cluster.setSampleString("Cluster");
        cluster.setSampleText("This is the Cluster Service test");
        cluster.setSampleDate(LocalDate.of(2012, 3, 26));
        cluster.setSampleTime(LocalTime.of(13, 26, 32));
        cluster.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        cluster.setSampleEnum(6);
        cluster.setSampleAmount("22.2");
        cluster.setSampleValid(true);
        cluster.setGmtCreate(new Date());
        clusterService.save(cluster);
        assertTrue(clusterService.existsById(1L));
    }

    @Test
    public void testFindById() {
        ClusterDO cluster = clusterService.findById(1L);
        assertNotNull(cluster.getSampleString());
    }

    @Test
    public void testDeleteById() {
        clusterService.deleteById(1L);
        assertFalse(clusterService.existsById(1L));
    }

}
