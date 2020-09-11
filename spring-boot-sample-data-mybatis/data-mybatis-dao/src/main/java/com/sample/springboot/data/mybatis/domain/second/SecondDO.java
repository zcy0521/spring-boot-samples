package com.sample.springboot.data.mybatis.domain.second;

import com.sample.springboot.data.mybatis.domain.second.base.BaseDO;
import com.sample.springboot.data.mybatis.enums.SampleEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.*;
import lombok.*;

@Table(name = "`mybatis_second`")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SecondDO extends BaseDO {
    @Column(name = "`sample_integer`")
    private Integer sampleInteger;

    @Column(name = "`sample_float`")
    private Float sampleFloat;

    @Column(name = "`sample_double`")
    private Double sampleDouble;

    @Column(name = "`sample_string`")
    private String sampleString;

    @Column(name = "`sample_date`")
    private LocalDate sampleDate;

    @Column(name = "`sample_time`")
    private LocalTime sampleTime;

    @Column(name = "`sample_datetime`")
    private LocalDateTime sampleDatetime;

    @Column(name = "`sample_enum`")
    private SampleEnum sampleEnum;

    @Column(name = "`sample_amount`")
    private BigDecimal sampleAmount;

    @Column(name = "`sample_valid`")
    private Boolean sampleValid;

    @Column(name = "`sample_text`")
    private String sampleText;
}