package com.sample.springboot.cache.redis.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@RedisHash("cache_redis_dept")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Dept {

    @Id
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private Long pid;

    private String deptName;

}
