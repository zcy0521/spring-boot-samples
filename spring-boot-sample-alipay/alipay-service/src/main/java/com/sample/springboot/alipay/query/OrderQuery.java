package com.sample.springboot.alipay.query;

import com.sample.springboot.alipay.enums.OrderStatus;
import com.sample.springboot.alipay.page.Page;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderQuery {

    private OrderStatus orderStatus;

    private Boolean deleted;

    /**
     * 分页查询后的页数信息
     */
    private Page page;

}
