package com.sample.springboot.rest.client.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.sample.springboot.rest.client.model.Sample;
import com.sample.springboot.rest.client.query.SampleQuery;
import com.sample.springboot.rest.client.service.SampleService;
import com.sample.springboot.rest.client.utils.HttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Sample> findAll(SampleQuery query) {
        if (query == null) {
            query = new SampleQuery();
        }

        // 序列化Query
        String queryJson;
        try {
            queryJson = objectMapper.writeValueAsString(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }

        // httpClient
        HttpClient httpClient = HttpClient
                .builder()
                .path("/api/samples")
                .parameter("query", queryJson)
                .build();

        // GET请求
        String response = httpClient.get();
        if (StringUtils.isBlank(response)) {
            return new ArrayList<>();
        }

        // 反序列化 SampleList
        List<Sample> sampleList = new ArrayList<>();
        try {
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Sample.class);
            sampleList = objectMapper.readValue(response, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sampleList;
    }

    @Override
    public Sample findById(Long id) {
        if (id == null) {
            return new Sample();
        }

        // httpClient
        HttpClient httpClient = HttpClient
                .builder()
                .path("/api/samples/" + id)
                .build();

        // GET请求
        String response = httpClient.get();
        if (StringUtils.isBlank(response)) {
            return new Sample();
        }

        // 反序列化 Sample
        Sample sample = new Sample();
        try {
            sample = objectMapper.readValue(response, Sample.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sample;
    }

    @Override
    public Long create(Sample sample) {
        if (sample == null) {
            return 0L;
        }

        // httpClient
        HttpClient httpClient = HttpClient
                .builder()
                .path("/api/samples")
                .build();

        // 序列化 Sample
        String sampleJson;
        try {
            sampleJson = objectMapper.writeValueAsString(sample);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return 0L;
        }

        // POST请求
        String response = httpClient.post(sampleJson);

        return StringUtils.isBlank(response) ? 0L : Long.valueOf(response);
    }

    @Override
    public boolean update(Sample sample) {
        if (sample == null || sample.getId() == null) {
            return false;
        }

        // httpClient
        HttpClient httpClient = HttpClient
                .builder()
                .path("/api/samples/" + sample.getId())
                .build();

        // 序列化Sample
        String sampleJson;
        try {
            sampleJson = objectMapper.writeValueAsString(sample);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }

        // PUT请求
        String response = httpClient.put(sampleJson);

        return !StringUtils.isBlank(response);
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        // httpClient
        HttpClient httpClient = HttpClient
                .builder()
                .path("/api/samples/" + id)
                .build();

        // DELETE请求
        String response = httpClient.delete();

        return !StringUtils.isBlank(response);
    }

    @Override
    public int deleteByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }

        // httpClient
        HttpClient httpClient = HttpClient
                .builder()
                .path("/api/samples/delete")
                .build();

        // 序列化ids
        String idsJson;
        try {
            idsJson = objectMapper.writeValueAsString(ids);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return 0;
        }

        // POST请求
        String response = httpClient.post(idsJson);

        return StringUtils.isBlank(response) ? 0 : Integer.valueOf(response);
    }

}
