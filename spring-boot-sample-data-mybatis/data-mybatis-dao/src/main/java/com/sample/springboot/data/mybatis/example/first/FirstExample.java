package com.sample.springboot.data.mybatis.example.first;

import com.sample.springboot.data.mybatis.enums.SampleEnum;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class FirstExample {

    private Integer sampleInteger;

    private String sampleString;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private LocalDate minDate;

    private LocalDate maxDate;

    private LocalDateTime minDateTime;

    private LocalDateTime maxDateTime;

    private SampleEnum[] sampleEnums;

    private Boolean deleted;

}
