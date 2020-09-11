package com.sample.springboot.data.redis.repository.cluster;

import com.sample.springboot.data.redis.config.ConnectionConfig;
import com.sample.springboot.data.redis.config.ClusterConfig;
import com.sample.springboot.data.redis.domain.cluster.ClusterDO;
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
@Import({ConnectionConfig.class, ClusterConfig.class})
public class ClusterRepositoryTest {

    @Autowired
    private ClusterRepository clusterRepository;

    @Test
    public void testSave() {
        ClusterDO cluster = new ClusterDO();
        cluster.setId(1L);
        cluster.setSampleInteger(222);
        cluster.setSampleFloat(33.3f);
        cluster.setSampleDouble(55.5d);
        cluster.setSampleString("Cluster");
        cluster.setSampleText("This is the Cluster Repository test");
        cluster.setSampleDate(LocalDate.of(2012, 3, 26));
        cluster.setSampleTime(LocalTime.of(13, 26, 32));
        cluster.setSampleDateTime(LocalDateTime.of(2018, 8, 9, 12, 22, 19));
        cluster.setSampleEnum(6);
        cluster.setSampleAmount("22.2");
        cluster.setSampleValid(true);
        cluster.setGmtCreate(new Date());
        clusterRepository.save(cluster);
        assertTrue(clusterRepository.existsById(1L));
    }

    @Test
    public void testFindById() {
        ClusterDO cluster = clusterRepository.findById(1L).orElse(new ClusterDO());
        assertNotNull(cluster.getSampleString());
    }

    @Test
    public void testDeleteById() {
        clusterRepository.deleteById(1L);
        assertFalse(clusterRepository.existsById(1L));
    }

    @Test
    public void testDeleteAll() {
        clusterRepository.deleteAll();
        assertEquals(0, clusterRepository.count());
    }

}
