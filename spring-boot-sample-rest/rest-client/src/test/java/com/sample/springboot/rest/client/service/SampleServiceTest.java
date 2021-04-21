package com.sample.springboot.rest.client.service;

import com.google.common.collect.Sets;
import com.sample.springboot.rest.client.model.Sample;
import com.sample.springboot.rest.client.query.SampleQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleServiceTest {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Autowired
    private SampleService sampleService;

    @Test
    public void testFindAll() {
        SampleQuery query = new SampleQuery();
        List<Sample> sampleList = sampleService.findAll(query);
        assertThat(sampleList, hasSize(10));

        query = new SampleQuery(2, 8);
        query.setSampleEnums(new Integer[]{3, 6});
        sampleList = sampleService.findAll(query);
        assertThat(sampleList, hasSize(8));
    }

    @Test
    public void testFindById() {
        Sample sample = sampleService.findById(1L);
        assertThat(sample, notNullValue());
    }

    @Test
    public void testCreate() {
        Sample sample = new Sample();
        sample.setSampleString("rest-client");
        sample.setSampleAmount(new BigDecimal("22.33").toString());
        sample.setSampleDate(LocalDate.of(1949, 10, 1).format(dateFormatter));
        sample.setSampleDateTime(LocalDateTime.of(2020, 10, 1, 11, 33, 22).format(dateTimeFormatter));
        sample.setSampleEnum(3);
        sample.setSampleText("rest-client Text");

        Long id = sampleService.create(sample);
        assertThat(id, greaterThan(0L));
    }

    @Test
    public void testUpdate() {
        Sample sample = new Sample();
        sample.setId(1L);
        sample.setSampleString("rest-client");
        sample.setSampleAmount(new BigDecimal("22.33").toString());
        sample.setSampleDate(LocalDate.of(1949, 10, 1).format(dateFormatter));
        sample.setSampleDateTime(LocalDateTime.of(2020, 10, 1, 11, 33, 22).format(dateTimeFormatter));
        sample.setSampleEnum(3);
        sample.setSampleText("rest-client Text");

        boolean succeed = sampleService.update(sample);
        assertThat(succeed, is(true));
    }

    @Test
    public void testDeleteById() {
        boolean succeed = sampleService.deleteById(1L);
        assertThat(succeed, is(true));
    }

    @Test
    public void testDeleteByIds() {
        int count = sampleService.deleteByIds(Sets.newHashSet(2L, 22L, 32L));
        assertThat(count, is(3));
    }

}
