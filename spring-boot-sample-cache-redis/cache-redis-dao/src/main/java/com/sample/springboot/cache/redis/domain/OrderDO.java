package com.sample.springboot.cache.redis.domain;

import com.sample.springboot.cache.redis.domain.base.BaseDO;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderDO extends BaseDO {

    private String subject;

    private BigDecimal totalAmount;

    private Long userId;

    private UserDO user;
}