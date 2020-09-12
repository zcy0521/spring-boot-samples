package com.sample.springboot.cache.redis.domain;

import com.sample.springboot.cache.redis.domain.base.BaseDO;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.*;

@Table(name = "`cache_redis_order`")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderDO extends BaseDO {
    @Column(name = "`subject`")
    private String subject;

    @Column(name = "`total_amount`")
    private BigDecimal totalAmount;

    @Column(name = "`user_id`")
    private Long userId;

    @Transient
    private UserDO user;
}