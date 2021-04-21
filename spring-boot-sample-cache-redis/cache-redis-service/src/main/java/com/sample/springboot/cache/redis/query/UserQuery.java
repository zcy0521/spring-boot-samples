package com.sample.springboot.cache.redis.query;

import com.sample.springboot.cache.redis.query.base.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends BaseQuery {

    private String userName;

    private Long deptId;

    private Set<Long> deptIds;

    private Boolean deleted;

}
