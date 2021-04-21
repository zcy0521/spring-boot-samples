package com.sample.springboot.rest.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class SampleVO {

    private Long id;

    private Integer sampleInteger;

    private Float sampleFloat;

    private Double sampleDouble;

    private String sampleString;

    private String sampleAmount;

    private String sampleDate;

    private String sampleDateTime;

    private Integer sampleEnum;

    private String sampleText;

    private Boolean deleted;

    @Data
    @EqualsAndHashCode
    public static class QueryVO {

        /**
         * 默认查询第1页
         */
        private int number = 1;

        /**
         * 默认每页显示9条
         */
        private int size = 10;

        /**
         * 默认查询有效记录
         */
        private Boolean deleted = false;

        private Integer sampleInteger;

        private String sampleString;

        private String minAmount;

        private String maxAmount;

        private String minDate;

        private String maxDate;

        private String minDateTime;

        private String maxDateTime;

        private Integer[] sampleEnums;

    }

}
