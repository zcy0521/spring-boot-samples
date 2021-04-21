package com.sample.springboot.rest.server.orika.mapper;

import com.sample.springboot.rest.server.enums.SampleEnum;
import com.sample.springboot.rest.server.model.SampleVO;
import com.sample.springboot.rest.server.orika.SampleVOMapper;
import com.sample.springboot.rest.server.query.SampleQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleVOMapperTest {

    @Autowired
    private SampleVOMapper sampleMapper;

    @Test
    public void testConvertToQuery() {
        SampleVO.QueryVO queryVO = new SampleVO.QueryVO();
        queryVO.setSampleString("1");
        queryVO.setMaxDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryVO.setSampleEnums(new Integer[]{SampleEnum.ENUM_A.value(), SampleEnum.ENUM_B.value()});

        SampleQuery query = sampleMapper.toQuery(queryVO);
        assertThat(query, notNullValue());
        assertThat(query.getSampleEnums(), is(new SampleEnum[]{SampleEnum.ENUM_A, SampleEnum.ENUM_B}));
    }

}
