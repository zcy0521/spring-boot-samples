package com.sample.springboot.rest.server.runner;

import com.sample.springboot.rest.server.domain.SampleDO;
import com.sample.springboot.rest.server.enums.SampleEnum;
import com.sample.springboot.rest.server.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "init-data", name = "sample", havingValue = "true")
public class InitialSampleRunner implements ApplicationRunner {

    @Autowired
    private SampleService sampleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 删除全部数据
        int count = sampleService.deleteAll();
        log.warn("删除Sample记录 {} 条", count);

        // 插入120条记录
        int sampleNum = 120;
        for (int i = 0; i < sampleNum; i++) {
            SampleDO sample = new SampleDO();
            sample.setSampleInteger(i + 1);
            sample.setSampleFloat(RandomUtils.nextFloat(0.1f, 10f));
            sample.setSampleDouble(RandomUtils.nextDouble(0.2d, 20d));
            sample.setSampleString("SampleString" + (i + 1));
            sample.setSampleAmount(new BigDecimal(RandomUtils.nextDouble(0.01d, 1000d)));
            sample.setSampleDate(getRandomDate());
            sample.setSampleDateTime(LocalDateTime.of(getRandomDate(), getRandomTime()));
            sample.setSampleEnum(getRandomSampleEnum());
            sample.setSampleText("SampleText" + (i + 1));
            sampleService.insert(sample);
        }

        log.info("数据初始化完成");
    }

    private static LocalDate getRandomDate() {
        LocalDate startDate = LocalDate.of(1949, 10, 1);
        LocalDate endDate = LocalDate.of(2020, 10, 1);
        long randomDate = ThreadLocalRandom.current().nextLong(startDate.toEpochDay(), endDate.toEpochDay());
        return LocalDate.ofEpochDay(randomDate);
    }

    private static LocalTime getRandomTime() {
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 0);
        long randomTime = ThreadLocalRandom.current().nextLong(startTime.toSecondOfDay(), endTime.toSecondOfDay());
        return LocalTime.ofSecondOfDay(randomTime);
    }

    private static SampleEnum getRandomSampleEnum() {
        SampleEnum[] enums = SampleEnum.values();
        int index = RandomUtils.nextInt(0, enums.length);
        return enums[index];
    }
}
