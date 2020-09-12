package com.sample.springboot.cache.redis.domain;

import com.sample.springboot.cache.redis.domain.base.BaseDO;
import javax.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "`cache_redis_user`")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseDO {

    @Column(name = "`user_name`")
    private String userName;

    @Column(name = "`dept_id`")
    private Long deptId;

    @Transient
    private DeptDO dept;

    @Transient
    private List<OrderDO> orders;

    @Transient
    private List<RoleDO> roles;

}