package com.sample.springboot.mapstruct.domain;

import com.sample.springboot.mapstruct.domain.base.BaseDO;
import com.sample.springboot.mapstruct.enums.SampleEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

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