package com.sample.springboot.cache.redis.example;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class UserRoleExample {

    private Long userId;

    private Set<Long> userIds;

    private Long roleId;

    private Set<Long> roleIds;

    private Boolean deleted;

}
