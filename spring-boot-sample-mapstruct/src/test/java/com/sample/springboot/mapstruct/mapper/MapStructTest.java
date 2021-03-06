package com.sample.springboot.mapstruct.mapper;

import com.sample.springboot.mapstruct.domain.SampleDO;
import com.sample.springboot.mapstruct.enums.SampleEnum;
import com.sample.springboot.mapstruct.model.SampleVO;
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
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapStructTest {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Autowired
    private SampleVOMapper mapper;

    @Test
    public void testConvertTo() {
        SampleVO sampleVO = createSampleVO(1L);
        SampleDO sample = mapper.convertTo(sampleVO);

        assertThat(sample.getId(), is(sampleVO.getId()));
        assertThat(sample.getDeleted(), is(sampleVO.getDeleted()));
        assertThat(sample.getSampleString(), is(sampleVO.getSampleString()));
        assertThat(sample.getSampleAmount(), is(new BigDecimal(sampleVO.getSampleAmount())));
        assertThat(sample.getSampleDate(), is(LocalDate.parse(sampleVO.getSampleDate(), dateFormatter)));
        assertThat(sample.getSampleDateTime(), is(LocalDateTime.parse(sampleVO.getSampleDateTime(), dateTimeFormatter)));
        assertThat(sample.getSampleEnum(), is(SampleEnum.valueOf(sampleVO.getSampleEnum())));
        assertThat(sample.getSampleText(), is(sampleVO.getSampleText()));
    }

    @Test
    public void testConvertToList() {
        List<SampleVO> sampleVOs = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            sampleVOs.add(createSampleVO((long) i));
        }

        List<SampleDO> samples = mapper.convertToList(sampleVOs);
        for (SampleDO sample : samples) {
            Long id = sample.getId();
            int index = Math.toIntExact(id) - 1;
            SampleVO sampleVO = sampleVOs.get(index);

            assertThat(sample.getId(), is(sampleVO.getId()));
            assertThat(sample.getDeleted(), is(sampleVO.getDeleted()));
            assertThat(sample.getSampleString(), is(sampleVO.getSampleString()));
            assertThat(sample.getSampleAmount(), is(new BigDecimal(sampleVO.getSampleAmount())));
            assertThat(sample.getSampleDate(), is(LocalDate.parse(sampleVO.getSampleDate(), dateFormatter)));
            assertThat(sample.getSampleDateTime(), is(LocalDateTime.parse(sampleVO.getSampleDateTime(), dateTimeFormatter)));
            assertThat(sample.getSampleEnum(), is(SampleEnum.valueOf(sampleVO.getSampleEnum())));
            assertThat(sample.getSampleText(), is(sampleVO.getSampleText()));
        }
    }

    @Test
    public void testUpdateTo() {
        SampleVO source = createSampleVO(1L);
        SampleDO target = createSampleDO(1L);

        source.setSampleString(null);
        source.setSampleText(null);
        source.setSampleDateTime(null);
        mapper.updateTo(source, target);

        assertThat(target.getId(), is(source.getId()));
        assertThat(target.getDeleted(), is(source.getDeleted()));
        assertThat(target.getSampleString(), notNullValue());
        assertThat(target.getSampleAmount(), is(new BigDecimal(source.getSampleAmount())));
        assertThat(target.getSampleDate(), is(LocalDate.parse(source.getSampleDate(), dateFormatter)));
        assertThat(target.getSampleDateTime(), notNullValue());
        assertThat(target.getSampleEnum(), is(SampleEnum.valueOf(source.getSampleEnum())));
        assertThat(target.getSampleText(), notNullValue());
    }

    @Test
    public void testConvertFrom() {
        SampleDO sample = createSampleDO(1L);
        SampleVO sampleVO = mapper.convertFrom(sample);

        assertThat(sampleVO.getId(), is(sample.getId()));
        assertThat(sampleVO.getDeleted(), is(sample.getDeleted()));
        assertThat(sampleVO.getSampleString(), is(sample.getSampleString()));
        assertThat(sampleVO.getSampleAmount(), is(sample.getSampleAmount().toString()));
        assertThat(sampleVO.getSampleDate(), is(sample.getSampleDate().format(dateFormatter)));
        assertThat(sampleVO.getSampleDateTime(), is(sample.getSampleDateTime().format(dateTimeFormatter)));
        assertThat(sampleVO.getSampleEnum(), is(sample.getSampleEnum().getValue()));
        assertThat(sampleVO.getSampleText(), is(sample.getSampleText()));
    }

    @Test
    public void testConvertFromList() {
        List<SampleDO> samples = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            samples.add(createSampleDO((long) i));
        }

        List<SampleVO> sampleVOs = mapper.convertFromList(samples);
        for (SampleVO sampleVO : sampleVOs) {
            Long id = sampleVO.getId();
            int index = Math.toIntExact(id) - 1;
            SampleDO sample = samples.get(index);

            assertThat(sampleVO.getId(), is(sample.getId()));
            assertThat(sampleVO.getDeleted(), is(sample.getDeleted()));
            assertThat(sampleVO.getSampleString(), is(sample.getSampleString()));
            assertThat(sampleVO.getSampleAmount(), is(sample.getSampleAmount().toString()));
            assertThat(sampleVO.getSampleDate(), is(sample.getSampleDate().format(dateFormatter)));
            assertThat(sampleVO.getSampleDateTime(), is(sample.getSampleDateTime().format(dateTimeFormatter)));
            assertThat(sampleVO.getSampleEnum(), is(sample.getSampleEnum().getValue()));
            assertThat(sampleVO.getSampleText(), is(sample.getSampleText()));
        }
    }

    @Test
    public void testUpdateFrom() {
        SampleDO source = createSampleDO(1L);
        SampleVO target = createSampleVO(1L);

        source.setSampleString(null);
        source.setSampleText(null);
        source.setSampleDateTime(null);
        mapper.updateFrom(source, target);

        assertThat(target.getId(), is(source.getId()));
        assertThat(target.getDeleted(), is(source.getDeleted()));
        assertThat(target.getSampleString(), notNullValue());
        assertThat(target.getSampleAmount(), is(source.getSampleAmount().toString()));
        assertThat(target.getSampleDate(), is(source.getSampleDate().format(dateFormatter)));
        assertThat(target.getSampleDateTime(), notNullValue());
        assertThat(target.getSampleEnum(), is(source.getSampleEnum().getValue()));
        assertThat(target.getSampleText(), notNullValue());
    }

    private SampleDO createSampleDO(Long id) {
        SampleDO sample = new SampleDO();
        sample.setId(id);
        sample.setGmtCreate(new Date());
        sample.setDeleted(false);
        sample.setSampleString("SampleDOString" + id);
        sample.setSampleAmount(getRandomBigDecimal());
        sample.setSampleDate(getRandomDate());
        sample.setSampleDateTime(LocalDateTime.of(getRandomDate(), getRandomTime()));
        sample.setSampleEnum(getRandomSampleEnum());
        sample.setSampleText("SampleDOText" + id);
        return sample;
    }

    private SampleVO createSampleVO(Long id) {
        SampleVO sampleVO = new SampleVO();
        sampleVO.setId(id);
        sampleVO.setDeleted(true);
        sampleVO.setSampleString("SampleVOString" + id);
        sampleVO.setSampleAmount(getRandomBigDecimal().toString());
        sampleVO.setSampleDate(getRandomDate().format(dateFormatter));
        sampleVO.setSampleDateTime(LocalDateTime.of(getRandomDate(), getRandomTime()).format(dateTimeFormatter));
        sampleVO.setSampleEnum(getRandomSampleEnum().getValue());
        sampleVO.setSampleText("SampleVOText" + id);
        return sampleVO;
    }

    private static BigDecimal getRandomBigDecimal() {
        String a = String.valueOf(RandomUtils.nextInt(0, 9999));
        String b = String.valueOf(RandomUtils.nextInt(0, 99));
        return new BigDecimal(a + "." + StringUtils.leftPad(b, 2, "0"));
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
