package com.sample.springboot.view.velocity.mapstruct.query;

import com.sample.springboot.view.velocity.example.SampleExample;
import com.sample.springboot.view.velocity.query.SampleQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SampleQueryMapper {

    SampleExample toSampleExample(SampleQuery query);

}
