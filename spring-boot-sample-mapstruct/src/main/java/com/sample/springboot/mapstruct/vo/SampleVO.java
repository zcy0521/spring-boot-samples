package com.sample.springboot.mapstruct.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
public class SampleVO {

    private Long id;

    private Integer sampleInteger;

    private Float sampleFloat;

    private Double sampleDouble;

    private String sampleString;

    private String sampleText;

    private String sampleDate;

    private String sampleTime;

    private String sampleDatetime;

    private String sampleAmount;

    private Integer sampleEnum;

    private Boolean sampleDisable;

}
