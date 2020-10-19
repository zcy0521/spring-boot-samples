package com.sample.springboot.cache.redis.domain;

import com.sample.springboot.cache.redis.domain.base.BaseDO;
import com.sample.springboot.cache.redis.enums.Position;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseDO {

    private String userName;

    private Long deptId;

    private Position position;

    private DeptDO dept;

    private List<RoleDO> roles;

    private List<OrderDO> orders;

}