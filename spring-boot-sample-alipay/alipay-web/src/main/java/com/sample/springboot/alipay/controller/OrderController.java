package com.sample.springboot.alipay.controller;

import com.sample.springboot.alipay.domain.OrderDO;
import com.sample.springboot.alipay.enums.OrderStatus;
import com.sample.springboot.alipay.model.OrderVO;
import com.sample.springboot.alipay.orika.mapper.OrderVOMapper;
import com.sample.springboot.alipay.page.Page;
import com.sample.springboot.alipay.query.OrderQuery;
import com.sample.springboot.alipay.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderVOMapper orderMapper;

    /**
     * 分页查询列表
     * @param number 查询第几页
     * @param size 每页记录数
     * @param query 查询条件
     */
    @GetMapping
    public String list(@ModelAttribute("query") OrderVO query,
                       @RequestParam(value = "number", required = false, defaultValue = "1") int number,
                       @RequestParam(value = "size", required = false, defaultValue = "11") int size,
                       Model model) {

        // 查询条件
        OrderQuery orderQuery = OrderQuery.builder()
                // 根据订单状态查询
                .orderStatus(null == query.getOrderStatus() ? null : OrderStatus.resolve(query.getOrderStatus()))
                // 默认查询未删除的订单
                .deleted(null == query.getDeleted() ? false : query.getDeleted())
                .build();

        // 查询列表
        List<OrderDO> orderList = orderService.findAllByExample(orderQuery, number, size);
        List<OrderVO> orderVOList = orderMapper.fromOrderList(orderList);
        model.addAttribute("orders", orderVOList);

        // 分页信息
        Page page = orderQuery.getPage();
        model.addAttribute("page", page);

        return "order/list.vm";
    }

}
