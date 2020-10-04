package com.sample.springboot.view.velocity.example;

import com.sample.springboot.view.velocity.enums.SampleEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SampleExample {

    private Integer sampleInteger;

    private String sampleString;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private LocalDate minDate;

    private LocalDate maxDate;

    private LocalDateTime minDateTime;

    private LocalDateTime maxDateTime;

    private SampleEnum[] sampleEnums;

    private Boolean disabled;

}
