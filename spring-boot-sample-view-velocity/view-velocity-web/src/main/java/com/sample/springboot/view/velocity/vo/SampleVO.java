package com.sample.springboot.view.velocity.vo;

import lombok.Data;

@Data
public class SampleVO {

    private Long id;

    private Long[] ids;

    private Integer sampleInteger;

    private Float sampleFloat;

    private Double sampleDouble;

    private String sampleString;

    private String sampleAmount;

    private String sampleDate;

    private String sampleDateTime;

    private Integer sampleEnum;

    private Boolean disabled;

    private String sampleText;

    private Query query = new Query();

    /**
     * 查询条件
     */
    @Data
    public class Query {

        /**
         * 默认查询第1页
         */
        private int number = 1;

        /**
         * 默认每页显示9条
         */
        private int size = 9;

        /**
         * 默认查询有效记录
         */
        private Boolean disabled = false;

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
