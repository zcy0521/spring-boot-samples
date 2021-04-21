package com.sample.springboot.cache.redis.query;

import com.sample.springboot.cache.redis.page.Page;
import com.sample.springboot.cache.redis.query.base.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderQuery extends BaseQuery {

    private String subject;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private Long userId;

    private Set<Long> userIds;

    private Boolean deleted;

}
