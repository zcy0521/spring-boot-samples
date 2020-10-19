package com.sample.springboot.data.mybatis.domain.first;

import com.sample.springboot.data.mybatis.domain.base.BaseDO;
import com.sample.springboot.data.mybatis.enums.SampleEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;

@Table(name = "`mybatis_first`")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FirstDO extends BaseDO {
    @Column(name = "`sample_integer`")
    private Integer sampleInteger;

    @Column(name = "`sample_float`")
    private Float sampleFloat;

    @Column(name = "`sample_double`")
    private Double sampleDouble;

    @Column(name = "`sample_string`")
    private String sampleString;

    @Column(name = "`sample_amount`")
    private BigDecimal sampleAmount;

    @Column(name = "`sample_date`")
    private LocalDate sampleDate;

    @Column(name = "`sample_date_time`")
    private LocalDateTime sampleDateTime;

    @Column(name = "`sample_enum`")
    private SampleEnum sampleEnum;

    @Column(name = "`sample_text`")
    private String sampleText;

}