package com.sample.springboot.alipay.domain;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.sample.springboot.alipay.domain.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AlipayConfigDO extends BaseDO {

    /**
     * 网关协议（固定https）。
     */
    private String protocol;

    /**
     * 支付宝网关（固定）。
     * 正式环境: openapi.alipay.com/gateway.do
     * 测试环境: openapi.alipaydev.com/gateway.do
     */
    private String gatewayHost;

    /**
     * 商户生成签名字符串所使用的签名算法类型（固定RSA2）。
     */
    private String signType;

    /**
     * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
     */
    private String notifyUrl;

    /**
     * 开发者的 app_id。支付宝分配给开发者的应用 ID。
     * VARCHAR(32)
     */
    private String appId;

    /**
     * 卖家支付宝用户号。一个商户可能有多个seller_id，以","分割。
     * VARCHAR(30)
     */
    private String sellerId;

    private Set<String> sellerIds;

    /**
     * 应用私钥
     */
    private String alipayPublicKey;

    /**
     * 应用私钥
     */
    private String merchantPrivateKey;

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;

        // 拆分sellerId
        if (StringUtils.isNotBlank(sellerId)) {
            List<String> sellerIdList = Splitter.on(',')
                    .trimResults()
                    .omitEmptyStrings()
                    .splitToList(sellerId);
            this.sellerIds = Sets.newHashSet(sellerIdList);
        }
    }
}
