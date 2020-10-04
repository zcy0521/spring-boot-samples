package com.sample.springboot.view.velocity.query;

import com.sample.springboot.view.velocity.enums.SampleEnum;
import com.sample.springboot.view.velocity.page.Page;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SampleQuery {

    /**
     * 分页信息
     */
    private Page page;

    /**
     * 查询条件
     */
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
