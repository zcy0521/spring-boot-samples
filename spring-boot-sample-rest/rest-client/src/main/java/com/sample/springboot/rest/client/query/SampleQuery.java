package com.sample.springboot.rest.client.query;

import lombok.Data;

@Data
public class SampleQuery {

    private int number;

    private int size;

    private Boolean deleted;

    private Integer sampleInteger;

    private String sampleString;

    private String minAmount;

    private String maxAmount;

    private String minDate;

    private String maxDate;

    private String minDateTime;

    private String maxDateTime;

    private Integer[] sampleEnums;

    public SampleQuery() {
        this.number = 1;
        this.size = 10;
    }

    public SampleQuery(int number, int size) {
        this.number = number;
        this.size = size;
    }
}
