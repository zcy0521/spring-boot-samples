package com.sample.springboot.cache.redis.domain.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseDO implements Serializable {

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private Boolean deleted;

}
