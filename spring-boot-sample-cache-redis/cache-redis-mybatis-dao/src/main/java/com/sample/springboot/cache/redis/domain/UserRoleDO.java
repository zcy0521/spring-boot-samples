package com.sample.springboot.cache.redis.domain;

import com.sample.springboot.cache.redis.domain.base.BaseDO;
import javax.persistence.*;
import lombok.*;

@Table(name = "`cache_redis_user_role`")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRoleDO extends BaseDO {
    @Column(name = "`user_id`")
    private Long userId;

    @Column(name = "`role_id`")
    private Long roleId;
}