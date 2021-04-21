package com.sample.springboot.alipay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.springboot.alipay.domain.AlipayNotifyDO;
import com.sample.springboot.alipay.model.AlipayNotifyVO;
import com.sample.springboot.alipay.orika.mapper.AlipayNotifyVOMapper;
import com.sample.springboot.alipay.service.AlipayNotifyService;
import com.sample.springboot.alipay.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "alipay")
public class AlipayNotifyController {

    @Autowired
    private AlipayNotifyService alipayNotifyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AlipayNotifyVOMapper alipayNotifyMapper;

    /**
     * 接收支付宝异步通知
     *
     * http://127.0.0.1:8080/alipay/receive_notify?total_amount=2.00&buyer_id=2088102116773037&body=%E5%A4%A7%E4%B9%90%E9%80%8F2.1&trade_no=2016071921001003030200089909&refund_fee=0.00&notify_time=2016-07-19%2014:10:49&subject=%E5%A4%A7%E4%B9%90%E9%80%8F2.1&sign_type=RSA2&charset=utf-8&notify_type=trade_status_sync&out_trade_no=0719141034-6418&gmt_close=2016-07-19%2014:10:46&gmt_payment=2016-07-19%2014:10:47&trade_status=TRADE_SUCCESS&version=1.0&sign=kPbQIjX+xQc8F0/A6/AocELIjhhZnGbcBN6G4MM/HmfWL4ZiHM6fWl5NQhzXJusaklZ1LFuMo+lHQUELAYeugH8LYFvxnNajOvZhuxNFbN2LhF0l/KL8ANtj8oyPM4NN7Qft2kWJTDJUpQOzCzNnV9hDxh5AaT9FPqRS6ZKxnzM=&gmt_create=2016-07-19%2014:10:44&app_id=2016091700534734&seller_id=2088102119685838&notify_id=4a91b7a78a503640467525113fb7d8bg8e
     */
    @RequestMapping("receive_notify")
    @ResponseBody
    public String receiveNotify(HttpServletRequest request) {
        // 异步通知验签
        Map<String,String> params = getRequestParams(request);
        boolean verifySuccess = alipayNotifyService.verify(params);

        if (verifySuccess) {
            // 将验签通过通知保存数据库
            AlipayNotifyVO notifyVO = objectMapper.convertValue(params, AlipayNotifyVO.class);
            AlipayNotifyDO notify = alipayNotifyMapper.toAlipayNotify(notifyVO);
            alipayNotifyService.save(notify);

            // 验签成功 params
            boolean updateOrderSuccess = orderService.updateByAlipayNotify(notify);
            if (updateOrderSuccess) {
                return "success";
            }
        }

        return "fail";
    }

    /**
     * 接收支付宝异步通知
     *
     * http://127.0.0.1:8080/alipay/receive_notify?total_amount=2.00&buyer_id=2088102116773037&body=%E5%A4%A7%E4%B9%90%E9%80%8F2.1&trade_no=2016071921001003030200089909&refund_fee=0.00&notify_time=2016-07-19%2014:10:49&subject=%E5%A4%A7%E4%B9%90%E9%80%8F2.1&sign_type=RSA2&charset=utf-8&notify_type=trade_status_sync&out_trade_no=0719141034-6418&gmt_close=2016-07-19%2014:10:46&gmt_payment=2016-07-19%2014:10:47&trade_status=TRADE_SUCCESS&version=1.0&sign=kPbQIjX+xQc8F0/A6/AocELIjhhZnGbcBN6G4MM/HmfWL4ZiHM6fWl5NQhzXJusaklZ1LFuMo+lHQUELAYeugH8LYFvxnNajOvZhuxNFbN2LhF0l/KL8ANtj8oyPM4NN7Qft2kWJTDJUpQOzCzNnV9hDxh5AaT9FPqRS6ZKxnzM=&gmt_create=2016-07-19%2014:10:44&app_id=2016091700534734&seller_id=2088102119685838&notify_id=4a91b7a78a503640467525113fb7d8bg8e
     */
    @PostMapping("receive_notify2")
    @ResponseBody
    public String receiveNotify2(HttpServletRequest request) {
        // 异步通知验签
        Map<String,String> params = getRequestParams(request);
        boolean verifySuccess = alipayNotifyService.verify2(params);

        if (verifySuccess) {
            // 将验签通过通知保存数据库
            AlipayNotifyVO notifyVO = objectMapper.convertValue(params, AlipayNotifyVO.class);
            AlipayNotifyDO notify = alipayNotifyMapper.toAlipayNotify(notifyVO);
            alipayNotifyService.save(notify);

            // 更新订单状态
            boolean updateOrderSuccess = orderService.updateByAlipayNotify(notify);
            if (updateOrderSuccess) {
                return "success";
            }
        }

        return "fail";
    }

    private Map<String,String> getRequestParams(HttpServletRequest request) {
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }

}
