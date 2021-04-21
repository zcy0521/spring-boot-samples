package com.sample.springboot.alipay.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode
public class OrderVO {

    private String outTradeNo;

    private BigDecimal totalAmount;

    private String subject;

    private String body;

    private Integer orderStatus;

    private Date gmtPayment;

    private Date gmtRefund;

    private Date gmtClose;

    private Integer productCode;

    private Boolean deleted;

}
