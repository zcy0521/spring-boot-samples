package com.sample.springboot.cache.redis.query;

import lombok.Data;

@Data
public class OrderQuery {

    private String subject;

    private Double minAmount;

    private Double maxAmount;

    private Long userId;

}
