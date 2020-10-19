package com.sample.springboot.cache.redis.example;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class UserExample {

    private Set<Long> ids;

    private String userName;

    private Long deptId;

    private Set<Long> deptIds;

    private Boolean deleted;

}
