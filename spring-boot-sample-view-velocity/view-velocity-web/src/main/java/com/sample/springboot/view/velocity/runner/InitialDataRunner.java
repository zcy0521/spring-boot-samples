package com.sample.springboot.view.velocity.runner;

import com.sample.springboot.view.velocity.domain.SampleDO;
import com.sample.springboot.view.velocity.enums.SampleEnum;
import com.sample.springboot.view.velocity.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class InitialDataRunner implements ApplicationRunner {

    @Autowired
    private SampleService sampleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 删除全部数据
        sampleService.deleteAll();

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
            sample.setDisabled(false);
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

    private static Long getRandomId(Set<Long> ids) {
        Long[] idArray = ids.toArray(new Long[0]);
        int index = RandomUtils.nextInt(0, ids.size());
        return idArray[index];
    }

    private static Set<Long> getRandomIds(Set<Long> ids, int num) {
        Long[] idArray = ids.toArray(new Long[0]);
        if (idArray.length < num) {
            return ids;
        }

        Set<Long> randomIds = new HashSet<>(num);
        while (randomIds.size() < num) {
            int index = RandomUtils.nextInt(0, ids.size());
            Long randomId = idArray[index];
            randomIds.add(randomId);
        }
        return randomIds;
    }
}
