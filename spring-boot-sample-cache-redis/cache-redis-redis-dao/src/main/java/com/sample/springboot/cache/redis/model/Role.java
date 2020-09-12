package com.sample.springboot.cache.redis.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@RedisHash("cache_redis_role")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Role {

    @Id
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String roleName;

}
