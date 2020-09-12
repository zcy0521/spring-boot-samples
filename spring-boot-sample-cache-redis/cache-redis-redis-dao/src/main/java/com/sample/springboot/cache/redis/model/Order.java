package com.sample.springboot.cache.redis.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@RedisHash("cache_redis_order")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Order {

    @Id
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String subject;

    private String totalAmount;

    private Long userId;

}
