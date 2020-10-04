package com.sample.springboot.view.velocity.mapstruct.query;

import com.sample.springboot.view.velocity.example.SampleExample;
import com.sample.springboot.view.velocity.query.SampleQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleQueryMapperTest {

    @Autowired
    private SampleQueryMapper mapper;

    @Test
    public void testToSampleExample() {
        SampleQuery query = new SampleQuery();
        SampleExample example = mapper.toSampleExample(query);
    }

}
