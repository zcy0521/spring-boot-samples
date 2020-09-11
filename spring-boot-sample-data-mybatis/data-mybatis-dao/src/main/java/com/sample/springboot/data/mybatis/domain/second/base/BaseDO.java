package com.sample.springboot.data.mybatis.domain.second.base;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="`id`", unique=true, nullable=false)
    private Long id;

    @Column(name="`gmt_create`")
    private Date gmtCreate;

    @Column(name="`gmt_modified`")
    private Date gmtModified;

}
