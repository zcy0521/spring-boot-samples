package com.sample.springboot.alipay.mapper.sql;

import com.sample.springboot.alipay.domain.AlipayConfigDO;
import com.sample.springboot.alipay.mybatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;

public class AlipayConfigSQLProvider {

    private static final String TABLE_NAME = "`alipay_config`";

    public String selectAll() {
        return new SQL()
                .SELECT("`id`")
                .SELECT("`app_id`")
                .SELECT("`seller_id`")
                .SELECT("`alipay_public_key`")
                .SELECT("`merchant_private_key`")
                .FROM(TABLE_NAME)
                .toString();
    }

    public String selectById(Long id) {
        return new SQL()
                .SELECT_ALL()
                .FROM(TABLE_NAME)
                .WHERE_ID()
                .toString();
    }

    public String insert(AlipayConfigDO entity) {
        return new SQL()
                .INSERT_INTO(TABLE_NAME)
                .VALUES("`app_id`", "#{appId, jdbcType=VARCHAR}")
                .VALUES("`seller_id`", "#{sellerId, jdbcType=VARCHAR}")
                .VALUES("`alipay_public_key`", "#{alipayPublicKey, jdbcType=VARCHAR}")
                .VALUES("`merchant_private_key`", "#{merchantPrivateKey, jdbcType=VARCHAR}")
                .toString();
    }

    public String update(AlipayConfigDO entity) {
        SQL sql = new SQL().UPDATE(TABLE_NAME);

        if (null != entity && StringUtils.isNotBlank(entity.getAppId())) {
            sql.SET("`app_id` = #{appId, jdbcType=VARCHAR}");
        }
        if (null != entity && StringUtils.isNotBlank(entity.getSellerId())) {
            sql.SET("`seller_id` = #{sellerId, jdbcType=VARCHAR}");
        }
        if (null != entity && null != entity.getAlipayPublicKey()) {
            sql.SET("`alipay_public_key` = #{alipayPublicKey, jdbcType=VARCHAR}");
        }
        if (null != entity && null != entity.getMerchantPrivateKey()) {
            sql.SET("`merchant_private_key` = #{merchantPrivateKey, jdbcType=VARCHAR}");
        }

        return sql.WHERE_ID().toString();
    }

}
