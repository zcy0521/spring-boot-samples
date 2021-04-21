package com.sample.springboot.alipay.mapper;

import com.sample.springboot.alipay.config.MyBatisConfig;
import com.sample.springboot.alipay.domain.AlipayConfigDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@MybatisTest
@Import({MyBatisConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AlipayConfigMapperTest {

    @Autowired
    private AlipayConfigMapper mapper;

    @Test
    public void testInsert() {
        AlipayConfigDO config = new AlipayConfigDO();
        config.setAppId("aaa");
        config.setAlipayPublicKey("aaa");
        config.setMerchantPrivateKey("aaa");
        int count = mapper.insert(config);
        assertThat(count, is(1));
    }

    @Test
    public void testUpdate() {
        AlipayConfigDO config = new AlipayConfigDO();
        config.setId(1L);
        config.setAppId("bbb");
        config.setAlipayPublicKey("bbb");
        config.setMerchantPrivateKey("bbb");
        int count = mapper.update(config);
        assertThat(count, is(1));

        config = mapper.selectById(1L);
        assertThat(config.getAppId(), is("bbb"));
        assertThat(config.getAlipayPublicKey(), is("bbb"));
        assertThat(config.getMerchantPrivateKey(), is("bbb"));
    }

    @Test
    public void testSelectAll() {
        List<AlipayConfigDO> configs = mapper.selectAll();
        assertThat(configs, not(empty()));
    }

}
