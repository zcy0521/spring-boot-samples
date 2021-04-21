package com.sample.springboot.alipay.example;

import com.sample.springboot.alipay.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderExample {

    private OrderStatus orderStatus;

    private Boolean deleted;

}
