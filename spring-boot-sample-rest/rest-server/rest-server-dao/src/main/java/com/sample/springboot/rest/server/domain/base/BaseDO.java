package com.sample.springboot.rest.server.domain.base;

import lombok.Data;

import java.util.Date;

@Data
public class BaseDO {

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private Boolean deleted;

}
