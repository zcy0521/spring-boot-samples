package com.sample.springboot.cache.redis.query;

import com.sample.springboot.cache.redis.page.Page;
import lombok.Data;

import java.util.Set;

@Data
public class UserQuery {

    private Page page;

    private String userName;

    private Long deptId;

    private Set<Long> deptIds;

    private Boolean deleted;

}
