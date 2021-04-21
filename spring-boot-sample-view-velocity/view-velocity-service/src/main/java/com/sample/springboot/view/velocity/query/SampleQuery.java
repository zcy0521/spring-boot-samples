package com.sample.springboot.view.velocity.query;

import com.sample.springboot.view.velocity.enums.SampleEnum;
import com.sample.springboot.view.velocity.page.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SampleQuery {

    /**
     * 默认查询当前页
     */
    private int number = 1;

    /**
     * 默认查询10条
     */
    private int size = 10;

    /**
     * 分页查询结果
     */
    private Page page;

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
