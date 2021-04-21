package com.sample.springboot.cache.redis.example;

import com.sample.springboot.cache.redis.enums.Position;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class DeptAdminExample {

    private Long deptId;

    private Set<Long> deptIds;

    private Long userId;

    private Set<Long> userIds;

    private Position position;

    private Position[] positions;

    private Boolean deleted;

}
