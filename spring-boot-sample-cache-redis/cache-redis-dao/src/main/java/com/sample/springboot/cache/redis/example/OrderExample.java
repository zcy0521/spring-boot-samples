package com.sample.springboot.cache.redis.example;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Builder
@Getter
public class OrderExample {

    private String subject;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private Long userId;

    private Set<Long> userIds;

    private Boolean deleted;

}
