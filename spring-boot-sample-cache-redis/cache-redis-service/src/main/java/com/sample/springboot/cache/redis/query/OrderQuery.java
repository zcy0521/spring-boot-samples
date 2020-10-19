package com.sample.springboot.cache.redis.query;

import com.sample.springboot.cache.redis.page.Page;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class OrderQuery {

    private Page page;

    private String subject;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private Long userId;

    private Set<Long> userIds;

    private Boolean deleted;

}
