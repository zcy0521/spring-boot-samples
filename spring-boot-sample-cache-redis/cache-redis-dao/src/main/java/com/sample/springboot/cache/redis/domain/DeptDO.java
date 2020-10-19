package com.sample.springboot.cache.redis.domain;

import com.sample.springboot.cache.redis.domain.base.BaseDO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DeptDO extends BaseDO {

    private Long parentId;

    private String deptName;

    private Integer level;

    private String path;

    private DeptDO parent;

    private List<DeptDO> children;

    private DeptDO[] ancestors;

    private List<UserDO> admins;

}