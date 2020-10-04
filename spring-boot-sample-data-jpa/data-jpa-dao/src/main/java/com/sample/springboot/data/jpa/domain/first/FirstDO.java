package com.sample.springboot.data.jpa.domain.first;

import com.sample.springboot.data.jpa.domain.second.base.BaseDO;
import com.sample.springboot.data.jpa.enums.SampleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "`jpa_first`")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FirstDO extends BaseDO {

    @Column(name="`sample_integer`")
    private Integer sampleInteger;

    @Column(name="`sample_float`")
    private Float sampleFloat;

    @Column(name="`sample_double`")
    private Double sampleDouble;

    @Column(name="`sample_string`")
    private String sampleString;

    @Column(name="`sample_amount`")
    private BigDecimal sampleAmount;

    @Column(name="`sample_date`", columnDefinition = "DATE")
    private LocalDate sampleDate;

    @Column(name="`sample_date_time`", columnDefinition = "TIMESTAMP")
    private LocalDateTime sampleDateTime;

    @Column(name="`sample_enum`")
    @Convert(converter = SampleEnum.Converter.class)
    private SampleEnum sampleEnum;

    @Column(name="`sample_text`", columnDefinition = "TEXT")
    private String sampleText;

    @Column(name="`disabled`")
    private Boolean disabled;

}
