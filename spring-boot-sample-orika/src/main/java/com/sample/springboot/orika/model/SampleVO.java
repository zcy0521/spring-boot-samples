package com.sample.springboot.orika.model;

import lombok.Data;

@Data
public class SampleVO {

    private Long id;

    private Boolean deleted;

    private Integer sampleInteger;

    private Float sampleFloat;

    private Double sampleDouble;

    private String sampleString;

    private String sampleAmount;

    private String sampleDate;

    private String sampleDateTime;

    private Integer sampleEnum;

    private String sampleText;

}
