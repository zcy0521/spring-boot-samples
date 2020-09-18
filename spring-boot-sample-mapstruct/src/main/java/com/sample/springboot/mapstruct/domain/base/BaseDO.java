package com.sample.springboot.mapstruct.domain.base;

import lombok.Data;

import java.util.Date;

@Data
public class BaseDO {

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

}
