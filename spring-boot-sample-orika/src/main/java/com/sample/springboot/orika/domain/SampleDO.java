package com.sample.springboot.orika.domain;

import com.sample.springboot.orika.domain.base.BaseDO;
import com.sample.springboot.orika.enums.SampleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SampleDO extends BaseDO {

    private Integer sampleInteger;

    private Float sampleFloat;

    private Double sampleDouble;

    private String sampleString;

    private BigDecimal sampleAmount;

    private LocalDate sampleDate;

    private LocalDateTime sampleDateTime;

    private SampleEnum sampleEnum;

    private Boolean disabled;

    private String sampleText;
}