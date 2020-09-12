package com.sample.springboot.cache.redis.domain;

import com.sample.springboot.cache.redis.domain.base.BaseDO;
import javax.persistence.*;
import lombok.*;

@Table(name = "`cache_redis_role`")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleDO extends BaseDO {
    @Column(name = "`role_name`")
    private String roleName;
}