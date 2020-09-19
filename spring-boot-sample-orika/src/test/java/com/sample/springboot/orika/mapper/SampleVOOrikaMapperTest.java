package com.sample.springboot.orika.mapper;

import com.sample.springboot.orika.domain.SampleDO;
import com.sample.springboot.orika.enums.SampleEnum;
import com.sample.springboot.orika.vo.SampleVO;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleVOOrikaMapperTest {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private SampleVOOrikaMapper sampleVOMapper;

    @Test
    public void testFrom() {
        SampleDO sample = createSampleDO(1L);
        SampleVO sampleVO = sampleVOMapper.from(sample);

        assertThat(sampleVO.getId(), is(sample.getId()));
        assertThat(sampleVO.getSampleString(), is(sample.getSampleString()));
        assertThat(sampleVO.getSampleText(), is(sample.getSampleText()));
        assertThat(sampleVO.getSampleDate(), is(sample.getSampleDate().format(dateFormatter)));
        assertThat(sampleVO.getSampleTime(), is(sample.getSampleTime().format(timeFormatter)));
        assertThat(sampleVO.getSampleDatetime(), is(sample.getSampleDatetime().format(dateTimeFormatter)));
        assertThat(sampleVO.getSampleAmount(), is(sample.getSampleAmount().toString()));
        assertThat(sampleVO.getSampleEnum(), is(sample.getSampleEnum().getValue()));
        assertThat(sampleVO.getSampleDisable(), is(sample.getSampleDisable()));
    }

    @Test
    public void testListFrom() {
        List<SampleDO> samples = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            samples.add(createSampleDO((long) i));
        }

        List<SampleVO> sampleVOs = sampleVOMapper.listFrom(samples);
        for (SampleVO sampleVO : sampleVOs) {
            Long id = sampleVO.getId();
            int index = Math.toIntExact(id) - 1;
            SampleDO sample = samples.get(index);

            assertThat(sampleVO.getId(), is(sample.getId()));
            assertThat(sampleVO.getSampleString(), is(sample.getSampleString()));
            assertThat(sampleVO.getSampleText(), is(sample.getSampleText()));
            assertThat(sampleVO.getSampleDate(), is(sample.getSampleDate().format(dateFormatter)));
            assertThat(sampleVO.getSampleTime(), is(sample.getSampleTime().format(timeFormatter)));
            assertThat(sampleVO.getSampleDatetime(), is(sample.getSampleDatetime().format(dateTimeFormatter)));
            assertThat(sampleVO.getSampleAmount(), is(sample.getSampleAmount().toString()));
            assertThat(sampleVO.getSampleEnum(), is(sample.getSampleEnum().getValue()));
            assertThat(sampleVO.getSampleDisable(), is(sample.getSampleDisable()));
        }
    }

    @Test
    public void testUpdateFrom() {
        SampleDO source = createSampleDO(1L);
        SampleVO target = createSampleVO(1L);

        source.setSampleString(null);
        source.setSampleText(null);
        source.setSampleDatetime(null);
        sampleVOMapper.updateFrom(source, target);

        assertThat(target.getSampleString(), is(notNullValue()));
        assertThat(target.getSampleText(), is(notNullValue()));
        assertThat(target.getSampleDatetime(), is(notNullValue()));

        assertThat(target.getId(), is(source.getId()));
        assertThat(target.getSampleDate(), is(source.getSampleDate().format(dateFormatter)));
        assertThat(target.getSampleTime(), is(source.getSampleTime().format(timeFormatter)));
        assertThat(target.getSampleAmount(), is(source.getSampleAmount().toString()));
        assertThat(target.getSampleEnum(), is(source.getSampleEnum().getValue()));
        assertThat(target.getSampleDisable(), is(source.getSampleDisable()));
    }

    @Test
    public void testTo() {
        SampleVO sampleVO = createSampleVO(1L);
        SampleDO sample = sampleVOMapper.to(sampleVO);

        assertThat(sample.getId(), is(sampleVO.getId()));
        assertThat(sample.getSampleString(), is(sampleVO.getSampleString()));
        assertThat(sample.getSampleText(), is(sampleVO.getSampleText()));
        assertThat(sample.getSampleDate(), is(LocalDate.parse(sampleVO.getSampleDate(), dateFormatter)));
        assertThat(sample.getSampleTime(), is(LocalTime.parse(sampleVO.getSampleTime(), timeFormatter)));
        assertThat(sample.getSampleDatetime(), is(LocalDateTime.parse(sampleVO.getSampleDatetime(), dateTimeFormatter)));
        assertThat(sample.getSampleAmount(), is(new BigDecimal(sampleVO.getSampleAmount())));
        assertThat(sample.getSampleEnum(), is(SampleEnum.of(sampleVO.getSampleEnum())));
        assertThat(sample.getSampleDisable(), is(sampleVO.getSampleDisable()));
    }

    @Test
    public void testListTo() {
        List<SampleVO> sampleVOs = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            sampleVOs.add(createSampleVO((long) i));
        }

        List<SampleDO> samples = sampleVOMapper.listTo(sampleVOs);
        for (SampleDO sample : samples) {
            Long id = sample.getId();
            int index = Math.toIntExact(id) - 1;
            SampleVO sampleVO = sampleVOs.get(index);

            assertThat(sample.getId(), is(sampleVO.getId()));
            assertThat(sample.getSampleString(), is(sampleVO.getSampleString()));
            assertThat(sample.getSampleText(), is(sampleVO.getSampleText()));
            assertThat(sample.getSampleDate(), is(LocalDate.parse(sampleVO.getSampleDate(), dateFormatter)));
            assertThat(sample.getSampleTime(), is(LocalTime.parse(sampleVO.getSampleTime(), timeFormatter)));
            assertThat(sample.getSampleDatetime(), is(LocalDateTime.parse(sampleVO.getSampleDatetime(), dateTimeFormatter)));
            assertThat(sample.getSampleAmount(), is(new BigDecimal(sampleVO.getSampleAmount())));
            assertThat(sample.getSampleEnum(), is(SampleEnum.of(sampleVO.getSampleEnum())));
            assertThat(sample.getSampleDisable(), is(sampleVO.getSampleDisable()));
        }
    }

    @Test
    public void testUpdateTo() {
        SampleVO source = createSampleVO(1L);
        SampleDO target = createSampleDO(1L);

        source.setSampleString(null);
        source.setSampleText(null);
        source.setSampleDatetime(null);
        sampleVOMapper.updateTo(source, target);

        assertThat(target.getSampleString(), is(notNullValue()));
        assertThat(target.getSampleText(), is(notNullValue()));
        assertThat(target.getSampleDatetime(), is(notNullValue()));

        assertThat(target.getId(), is(source.getId()));
        assertThat(target.getSampleDate(), is(LocalDate.parse(source.getSampleDate(), dateFormatter)));
        assertThat(target.getSampleTime(), is(LocalTime.parse(source.getSampleTime(), timeFormatter)));
        assertThat(target.getSampleAmount(), is(new BigDecimal(source.getSampleAmount())));
        assertThat(target.getSampleEnum(), is(SampleEnum.of(source.getSampleEnum())));
        assertThat(target.getSampleDisable(), is(source.getSampleDisable()));
    }



    private SampleDO createSampleDO(Long id) {
        SampleDO sample = new SampleDO();
        sample.setId(id);
        sample.setSampleString("SampleDOString" + id);
        sample.setSampleText("SampleDOText" + id);
        sample.setSampleDate(getRandomDate());
        sample.setSampleTime(getRandomTime());
        sample.setSampleDatetime(LocalDateTime.of(getRandomDate(), getRandomTime()));
        sample.setSampleEnum(getRandomSampleEnum());
        sample.setSampleAmount(getRandomBigDecimal());
        sample.setSampleDisable(false);
        sample.setGmtCreate(new Date());
        return sample;
    }

    private SampleVO createSampleVO(Long id) {
        SampleVO sampleVO = new SampleVO();
        sampleVO.setId(id);
        sampleVO.setSampleString("SampleVOString" + id);
        sampleVO.setSampleText("SampleVOText" + id);
        sampleVO.setSampleDate(getRandomDate().format(dateFormatter));
        sampleVO.setSampleTime(getRandomTime().format(timeFormatter));
        sampleVO.setSampleDatetime(LocalDateTime.of(getRandomDate(), getRandomTime()).format(dateTimeFormatter));
        sampleVO.setSampleEnum(getRandomSampleEnum().getValue());
        sampleVO.setSampleAmount(getRandomBigDecimal().toString());
        sampleVO.setSampleDisable(true);
        return sampleVO;
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

    private static BigDecimal getRandomBigDecimal() {
        String a = String.valueOf(RandomUtils.nextInt(0, 9999));
        String b = String.valueOf(RandomUtils.nextInt(0, 99));
        return new BigDecimal(a + "." + StringUtils.leftPad(b, 2, "0"));
    }

    private static SampleEnum getRandomSampleEnum() {
        SampleEnum[] enums = SampleEnum.values();
        int index = RandomUtils.nextInt(0, enums.length);
        return enums[index];
    }

}
