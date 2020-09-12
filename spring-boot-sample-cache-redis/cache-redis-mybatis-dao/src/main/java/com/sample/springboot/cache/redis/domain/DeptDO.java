package com.sample.springboot.cache.redis.domain;

import com.sample.springboot.cache.redis.domain.base.BaseDO;
import javax.persistence.*;
import lombok.*;

@Table(name = "`cache_redis_dept`")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DeptDO extends BaseDO {
    @Column(name = "`pid`")
    private Long pid;

    @Column(name = "`dept_name`")
    private String deptName;
}